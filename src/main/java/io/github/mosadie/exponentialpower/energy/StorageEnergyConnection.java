package io.github.mosadie.exponentialpower.energy;

import io.github.mosadie.exponentialpower.entities.StorageEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.energy.IEnergyStorage;

public class StorageEnergyConnection implements IEnergyStorage {
    private final StorageEntity owner;
    private final Direction direction;

    public StorageEnergyConnection(StorageEntity owner, Direction dir) {
        this.owner = owner;
        direction = dir;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        double energy = owner.getEnergy();
        double maxEnergy = owner.getMaxEnergy();
        if (energy >= maxEnergy) {
            return 0;
        }
        double energyReceived = Math.min(maxEnergy - energy, maxReceive);
        if (!simulate) {
            owner.setEnergy(energy + energyReceived);
        }
        owner.setChanged();
        owner.freeze(direction);
        return (int) energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        double energy = owner.getEnergy();
        if (energy <= 0) {
            return 0;
        }
        double energyExtracted = Math.min(energy, maxExtract);
        if (!simulate) {
            owner.setEnergy(energy - energyExtracted);
        }
        owner.setChanged();
        return (int) energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        double energy = owner.getEnergy();
        return (int) (energy > Integer.MAX_VALUE ? Integer.MAX_VALUE : energy);
    }

    @Override
    public int getMaxEnergyStored() {
        double maxEnergy = owner.getMaxEnergy();
        return (maxEnergy > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) maxEnergy);
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
