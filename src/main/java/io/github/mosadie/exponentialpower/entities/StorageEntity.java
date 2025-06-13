package io.github.mosadie.exponentialpower.entities;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.energy.StorageEnergyConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;

public class StorageEntity extends BlockEntity implements ICapabilityProvider {
    private final EnergyLevelConfig config;
    private double energy = 0;
    private final EnumMap<Direction, Boolean> freezeExpend = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, LazyOptional<StorageEnergyConnection>> fecOptional = new EnumMap<>(Direction.class);

    public StorageEntity(BlockPos pos, BlockState state, EnergyLevelConfig config) {
        super(config.getStorageBlockEntityType(), pos, state);
        this.config = config;
        for (Direction direction : Direction.values()) {
            freezeExpend.put(direction, false);
            fecOptional.put(direction, LazyOptional.of(() -> new StorageEnergyConnection(this, direction)));
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putDouble("energy", energy);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        energy = tag.getDouble("energy");
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == ForgeCapabilities.ENERGY) {
            return fecOptional.get(direction != null ? direction : Direction.UP).cast();
        }
        return super.getCapability(capability, direction);
    }

    public static <T extends BlockEntity> void tick(T tile) {
        if (tile instanceof StorageEntity storage) {
            if (storage.energy > 0) {
                storage.handleSendingEnergy();
            }
        }
    }

    private void handleSendingEnergy() {
        Level level = this.level;
        if (level == null) {
            return;
        }
        if (level.isClientSide) {
            return;
        }
        for (Direction direction : Direction.values()) {
            if (freezeExpend.get(direction)) {
                freezeExpend.put(direction, false);
                continue;
            }
            BlockPos pos = getBlockPos().relative(direction);
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity == null) {
                continue;
            }
            if (entity instanceof StorageEntity storage) {
                energy -= storage.acceptEnergy(energy);
                if (energy <= 0) {
                    return;
                }
                continue;
            }
            IEnergyStorage storage = entity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).resolve().filter(IEnergyStorage::canReceive).orElse(null);
            if (storage == null) {
                continue;
            }
            for (int i = 0; i < config.getStorageTransmissionCount(); i++) {
                energy -= storage.receiveEnergy((int) energy, false);
                if (energy <= 0) {
                    return;
                }
            }
        }
    }

    public double acceptEnergy(double energyOffered) {
        double maxEnergy = config.getStorageMaxEnergy();
        if (energy >= maxEnergy || energyOffered <= 0) {
            return 0;
        }
        if (energy + energyOffered >= maxEnergy) {
            double amountAccepted = maxEnergy - energy;
            energy = maxEnergy;
            return amountAccepted;
        }
        if (energy + energyOffered < 0 || energy + energyOffered > Double.MAX_VALUE) {
            double amountAccepted = Double.MAX_VALUE - energy;
            energy = Double.MAX_VALUE;
            return amountAccepted;
        }
        energy += energyOffered;
        return energyOffered;
    }

    public double getMaxEnergy() {
        return config.getStorageMaxEnergy();
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void freeze(Direction direction) {
        this.freezeExpend.put(direction, true);
    }
}
