package io.github.mosadie.exponentialpower.entities;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.energy.StorageEnergyConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class StorageEntity extends BlockEntity {
    private final EnergyLevelConfig config;
    private double energy = 0;
    private final EnumMap<Direction, Boolean> freezeExpend = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, StorageEnergyConnection> energyStorages = new EnumMap<>(Direction.class);

    public StorageEntity(BlockPos pos, BlockState state, EnergyLevelConfig config) {
        super(config.getStorageBlockEntityType(), pos, state);
        this.config = config;
        for (Direction direction : Direction.values()) {
            freezeExpend.put(direction, false);
            energyStorages.put(direction, new StorageEnergyConnection(this, direction));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putDouble("energy", energy);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        energy = tag.getDouble("energy");
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
            IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction.getOpposite());
            if (storage == null || !storage.canReceive()) {
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
            setChanged();
            return amountAccepted;
        }
        if (energy + energyOffered < 0 || energy + energyOffered > Double.MAX_VALUE) {
            double amountAccepted = Double.MAX_VALUE - energy;
            energy = Double.MAX_VALUE;
            setChanged();
            return amountAccepted;
        }
        energy += energyOffered;
        setChanged();
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
        setChanged();
    }

    public void freeze(Direction direction) {
        this.freezeExpend.put(direction, true);
    }

    public StorageEnergyConnection getEnergyStorage(Direction direction) {
        return energyStorages.get(direction != null ? direction : Direction.UP);
    }
}
