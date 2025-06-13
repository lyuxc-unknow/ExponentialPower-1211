package io.github.mosadie.exponentialpower;

import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class EnergyLevelConfig {
    public static final EnergyLevelConfig REGULAR = new EnergyLevelConfig() {
        @Override
        public BlockEntityType<?> getGeneratorBlockEntityType() {
            return Registration.ENDER_GENERATOR_BE.get();
        }

        @Override
        public Block getGeneratorBlock() {
            return Registration.ENDER_GENERATOR.get();
        }

        @Override
        public int getGeneratorMaxStack() {
            return Config.ENDER_GENERATOR_MAX_STACK.get();
        }

        @Override
        public int getGeneratorTransmissionCount() {
            return Config.ENDER_GENERATOR_TRANSMISSION_COUNT.get();
        }

        @Override
        public double calculateEnergy(int itemStackCount) {
            double base = Config.ENDER_GENERATOR_BASE.get();
            return longPow(base, 63 * (itemStackCount / (double) getGeneratorMaxStack())) - 1L;
        }

        @Override
        public int getGeneratorMaxDistance() {
            return Config.ENDER_GENERATOR_MAX_DISTANCE.get();
        }

        private long longPow(double a, double b) {
            if (b == 0) {
                return 1L;
            }
            if (b == 1) {
                return (long) a;
            }
            return (long) Math.pow(a, b);
        }

        @Override
        public BlockEntityType<?> getStorageBlockEntityType() {
            return Registration.ENDER_STORAGE_BE.get();
        }

        @Override
        public double getStorageMaxEnergy() {
            return Config.ENDER_STORAGE_MAX_ENERGY.get();
        }

        @Override
        public int getStorageTransmissionCount() {
            return Config.ENDER_STORAGE_TRANSMISSION_COUNT.get();
        }
    };

    public static final EnergyLevelConfig ADVANCED = new EnergyLevelConfig() {
        @Override
        public BlockEntityType<?> getGeneratorBlockEntityType() {
            return Registration.ADV_ENDER_GENERATOR_BE.get();
        }

        @Override
        public Block getGeneratorBlock() {
            return Registration.ADV_ENDER_GENERATOR.get();
        }

        @Override
        public int getGeneratorMaxStack() {
            return Config.ADV_ENDER_GENERATOR_MAX_STACK.get();
        }

        @Override
        public int getGeneratorTransmissionCount() {
            return Config.ADV_ENDER_GENERATOR_TRANSMISSION_COUNT.get();
        }

        @Override
        public int getGeneratorMaxDistance() {
            return Config.ADV_ENDER_GENERATOR_MAX_DISTANCE.get();
        }

        @Override
        public double calculateEnergy(int itemStackCount) {
            double base = Config.ADV_ENDER_GENERATOR_BASE.get();
            return Math.pow(base, 1023 * (itemStackCount / (double) getGeneratorMaxStack())) * (2 - Math.pow(2, -52));
        }

        @Override
        public BlockEntityType<?> getStorageBlockEntityType() {
            return Registration.ADV_ENDER_STORAGE_BE.get();
        }

        @Override
        public double getStorageMaxEnergy() {
            return Config.ADV_ENDER_STORAGE_MAX_ENERGY.get();
        }

        @Override
        public int getStorageTransmissionCount() {
            return Config.ADV_ENDER_STORAGE_TRANSMISSION_COUNT.get();
        }
    };

    public abstract BlockEntityType<?> getGeneratorBlockEntityType();

    public abstract Block getGeneratorBlock();

    public abstract int getGeneratorTransmissionCount();

    public abstract int getGeneratorMaxStack();

    public abstract int getGeneratorMaxDistance();

    public abstract double calculateEnergy(int itemStackCount);

    public abstract BlockEntityType<?> getStorageBlockEntityType();

    public abstract double getStorageMaxEnergy();

    public abstract int getStorageTransmissionCount();
}
