package io.github.mosadie.exponentialpower.energy;

import io.github.mosadie.exponentialpower.entities.GeneratorEntity;
import net.minecraftforge.energy.IEnergyStorage;

public class GeneratorEnergyConnection implements IEnergyStorage {
	private final GeneratorEntity owner;
	private final boolean canExtract;
	private final boolean canReceive;
	
	public GeneratorEnergyConnection(GeneratorEntity owner, boolean canExtract, boolean canReceive) {
		this.owner = owner;
		this.canExtract = canExtract;
		this.canReceive = canReceive;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (owner.energy <= maxExtract) {
			int temp = (int) owner.energy;
			if (!simulate) owner.energy = 0;
			owner.setChanged();
			return temp;
		}
		else {
			if (!simulate) owner.energy -= maxExtract;
			owner.setChanged();
			return maxExtract;
		}
	}

	@Override
	public int getEnergyStored() {
		return (int) (owner.energy > Integer.MAX_VALUE ? Integer.MAX_VALUE : owner.energy);
	}

	@Override
	public int getMaxEnergyStored() {
		return (int) (owner.currentOutput > Integer.MAX_VALUE ? Integer.MAX_VALUE : owner.currentOutput);
	}

	@Override
	public boolean canExtract() {
		return canExtract;
	}

	@Override
	public boolean canReceive() {
		return canReceive;
	}
}
