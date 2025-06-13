package io.github.mosadie.exponentialpower.energy;

import io.github.mosadie.exponentialpower.entities.GeneratorEntity;
import net.minecraftforge.energy.IEnergyStorage;

public class GeneratorEnergyConnection implements IEnergyStorage {
    private final GeneratorEntity owner;

    public GeneratorEnergyConnection(GeneratorEntity owner) {
        this.owner = owner;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        double energyExtracted = Math.min(owner.getEnergy(), maxExtract);
        if (!simulate) {
            owner.setEnergy(owner.getEnergy() - energyExtracted);
        }
        owner.setChanged();
        return (int) energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return (int) owner.getEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) owner.getCurrentOutput();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
