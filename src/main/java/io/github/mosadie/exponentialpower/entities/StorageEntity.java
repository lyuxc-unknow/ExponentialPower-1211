package io.github.mosadie.exponentialpower.entities;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.energy.StorageEnergyConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;

public class StorageEntity extends BlockEntity implements BlockEntityTicker<StorageEntity> {
    private final EnergyLevelConfig config;
    private double energy = 0;
    public EnumMap<Direction, Boolean> freezeExpend;
    private final EnumMap<Direction, StorageEnergyConnection> fec;
    private final EnumMap<Direction, LazyOptional<StorageEnergyConnection>> fecOptional;

    public StorageEntity(BlockPos pos, BlockState state, EnergyLevelConfig config) {
        super(config.getStorageBlockEntityType(), pos, state);
        this.config = config;
        freezeExpend = new EnumMap<>(Direction.class);
        fec = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            fec.put(dir, new StorageEnergyConnection(this, true, true, dir));
        }

        fecOptional = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            fecOptional.put(dir, LazyOptional.of(() -> fec.get(dir)));
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
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction dir) {
        if (cap == ForgeCapabilities.ENERGY) return fecOptional.get((dir != null) ? dir : Direction.UP).cast();
        return super.getCapability(cap, dir);
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull StorageEntity storage) {
        if (energy > 0) {
            handleSendingEnergy();
        }
    }

    private void handleSendingEnergy() {
        if (level == null) {
            return;
        }
        if (level.isClientSide) {
            return;
        }
        if (energy <= 0) {
            return;
        }
        for (Direction dir : Direction.values()) {
            if (!freezeExpend.containsKey(dir)) {
                freezeExpend.put(dir, false);
            }
            if (freezeExpend.get(dir)) {
                freezeExpend.put(dir, false);
                continue;
            }
            BlockPos targetBlock = getBlockPos().relative(dir);
            BlockEntity entity = level.getBlockEntity(targetBlock);
            if (entity == null) {
                continue;
            }
            if (entity instanceof StorageEntity storage) {
                double difference = storage.acceptEnergy(energy);
                energy -= difference;
                if (difference > 0) {
                    freezeExpend.put(dir, true);
                }
                continue;
            }
            entity.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).filter(IEnergyStorage::canReceive).ifPresent((cap) -> {
                int change = cap.receiveEnergy((int) (energy > Integer.MAX_VALUE ? Integer.MAX_VALUE : energy), false);
                if (change > 0) {
                    energy -= change;
                    freezeExpend.put(dir, true);
                }
            });
        }
    }

    public double acceptEnergy(double energyOffered) {
        double maxEnergy = config.getMaxEnergy();
        if (energy >= maxEnergy || energyOffered <= 0) {
            return 0;
        }
        if (energy + energyOffered > maxEnergy) {
            double amountAccepted = maxEnergy - energy;
            energy = maxEnergy;
            return amountAccepted;
        }
        if (energy + energyOffered < 0) {
            double amountAccepted = Double.MAX_VALUE - energy;
            energy = Double.MAX_VALUE;
            return amountAccepted;
        }
        energy += energyOffered;
        return energyOffered;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getEnergy() {
        return energy;
    }

    public double getMaxEnergy() {
        return config.getMaxEnergy();
    }
}
