package io.github.mosadie.exponentialpower;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

public class Config {

    public static final String CATEGORY_ENDER_GENERATOR = "generator";

    public static final String SUBCATEGORY_ENDER_GENERATOR_REGULAR = "regular";
    public static ModConfigSpec.DoubleValue ENDER_GENERATOR_BASE;
    public static ModConfigSpec.IntValue ENDER_GENERATOR_MAX_STACK;
    public static ModConfigSpec.IntValue ENDER_GENERATOR_TRANSMISSION_COUNT;
    public static ModConfigSpec.IntValue ENDER_GENERATOR_MAX_DISTANCE;

    public static final String SUBCATEGORY_ENDER_GENERATOR_ADVANCED = "advanced";
    public static ModConfigSpec.DoubleValue ADV_ENDER_GENERATOR_BASE;
    public static ModConfigSpec.IntValue ADV_ENDER_GENERATOR_MAX_STACK;
    public static ModConfigSpec.IntValue ADV_ENDER_GENERATOR_TRANSMISSION_COUNT;
    public static ModConfigSpec.IntValue ADV_ENDER_GENERATOR_MAX_DISTANCE;

    public static final String CATEGORY_ENDER_STORAGE = "storage";

    public static final String SUBCATEGORY_ENDER_STORAGE_REGULAR = "regular";
    public static ModConfigSpec.LongValue ENDER_STORAGE_MAX_ENERGY;
    public static ModConfigSpec.IntValue ENDER_STORAGE_TRANSMISSION_COUNT;

    public static final String SUBCATEGORY_ENDER_STORAGE_ADVANCED = "advanced";
    public static ModConfigSpec.DoubleValue ADV_ENDER_STORAGE_MAX_ENERGY;
    public static ModConfigSpec.IntValue ADV_ENDER_STORAGE_TRANSMISSION_COUNT;

    public static ModConfigSpec SERVER_CONFIG;

    static {
        ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

        SERVER_BUILDER.comment("Ender Generator Settings").push(CATEGORY_ENDER_GENERATOR);

        SERVER_BUILDER.push(SUBCATEGORY_ENDER_GENERATOR_REGULAR);
        ENDER_GENERATOR_BASE = SERVER_BUILDER.comment("Controls the rate of change of the power output. Remember Base^MaxStack-1 must be less than Long.MAX_VALUE for things to work correctly.").defineInRange("base", 2, 0, Double.MAX_VALUE);
        ENDER_GENERATOR_MAX_STACK = SERVER_BUILDER.comment("Controls the number of Ender Cells required to reach the maximum power output.").defineInRange("maxStack", 64, 1, 64);
        ENDER_GENERATOR_TRANSMISSION_COUNT = SERVER_BUILDER.comment("Control the count/tick of energy transmission.").defineInRange("transmissionCount", 10, 1, 100);
        ENDER_GENERATOR_MAX_DISTANCE = SERVER_BUILDER.comment("Controls the chebyshev distance of remote transmission of energy by the Generator.").defineInRange("maxDistance", 3, 1, 16);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push(SUBCATEGORY_ENDER_GENERATOR_ADVANCED);
        ADV_ENDER_GENERATOR_BASE = SERVER_BUILDER.comment("Controls the rate of change of the power output. Remember Base^MaxStack-1 must be less than Long.MAX_VALUE for things to work correctly.").defineInRange("base", 2, 0, Double.MAX_VALUE);
        ADV_ENDER_GENERATOR_MAX_STACK = SERVER_BUILDER.comment("Controls the number of Ender Cells required to reach the maximum power output.").defineInRange("maxStack", 64, 1, 64);
        ADV_ENDER_GENERATOR_TRANSMISSION_COUNT = SERVER_BUILDER.comment("Control the count/tick of energy transmission.").defineInRange("transmissionCount", 10, 1, 100);
        ADV_ENDER_GENERATOR_MAX_DISTANCE = SERVER_BUILDER.comment("Controls the chebyshev distance of remote transmission of energy by the Generator.").defineInRange("maxDistance", 5, 1, 16);
        SERVER_BUILDER.pop(2);

        SERVER_BUILDER.comment("Ender Storage Settigns").push(CATEGORY_ENDER_STORAGE);

        SERVER_BUILDER.push(SUBCATEGORY_ENDER_STORAGE_REGULAR);
        ENDER_STORAGE_MAX_ENERGY = SERVER_BUILDER.comment("The maximum amount of power that can be stored in a single Ender Storage block.").defineInRange("maxEnergy", Long.MAX_VALUE, 0, Long.MAX_VALUE);
        ENDER_STORAGE_TRANSMISSION_COUNT = SERVER_BUILDER.comment("Control the count/tick of energy transmission.").defineInRange("transmissionCount", 10, 1, 100);
        SERVER_BUILDER.pop().push(SUBCATEGORY_ENDER_STORAGE_ADVANCED);
        ADV_ENDER_STORAGE_MAX_ENERGY = SERVER_BUILDER.comment("The maximum amount of power that can be stored in a single Advanced Ender Storage block.").defineInRange("maxEnergy", Double.MAX_VALUE, 0, Double.MAX_VALUE);
        ADV_ENDER_STORAGE_TRANSMISSION_COUNT = SERVER_BUILDER.comment("Control the count/tick of energy transmission.").defineInRange("transmissionCount", 10, 1, 100);
        SERVER_BUILDER.pop(2);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    public static void init(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }
}
