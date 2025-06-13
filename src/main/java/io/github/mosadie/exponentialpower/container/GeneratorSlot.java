package io.github.mosadie.exponentialpower.container;

import io.github.mosadie.exponentialpower.entities.GeneratorEntity;
import net.minecraft.world.inventory.Slot;

public class GeneratorSlot extends Slot {
    private final int stackLimit;

    public GeneratorSlot(int stackLimit, GeneratorEntity generator, int index, int xPosition, int yPosition) {
        super(generator, index, xPosition, yPosition);
        this.stackLimit = stackLimit;
    }

    @Override
    public int getMaxStackSize() {
        return stackLimit;
    }
}
