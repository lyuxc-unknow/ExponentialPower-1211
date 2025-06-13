package io.github.mosadie.exponentialpower.setup;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.blocks.GeneratorBlock;
import io.github.mosadie.exponentialpower.blocks.StorageBlock;
import io.github.mosadie.exponentialpower.container.GeneratorContainerMenu;
import io.github.mosadie.exponentialpower.entities.GeneratorEntity;
import io.github.mosadie.exponentialpower.entities.StorageEntity;
import io.github.mosadie.exponentialpower.items.CellItem;
import io.github.mosadie.exponentialpower.items.GeneratorItem;
import io.github.mosadie.exponentialpower.items.StorageItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExponentialPower.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExponentialPower.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExponentialPower.MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExponentialPower.MODID);

    public static void init(FMLJavaModLoadingContext context) {
        BLOCKS.register(context.getModEventBus());
        ITEMS.register(context.getModEventBus());
        BLOCK_ENTITIES.register(context.getModEventBus());
        CONTAINERS.register(context.getModEventBus());
    }

    private static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(DyeColor.BLUE)
            .pushReaction(PushReaction.DESTROY)
            .strength(2.5f, 15.0f);

    // Blocks

    public static final RegistryObject<Block> ENDER_GENERATOR = BLOCKS.register("ender_generator", () -> new GeneratorBlock(BLOCK_PROPERTIES, EnergyLevelConfig.REGULAR));
    public static final RegistryObject<Block> ADV_ENDER_GENERATOR = BLOCKS.register("advanced_ender_generator", () -> new GeneratorBlock(BLOCK_PROPERTIES, EnergyLevelConfig.ADVANCED));
    public static final RegistryObject<Block> ENDER_STORAGE = BLOCKS.register("ender_storage", () -> new StorageBlock(BLOCK_PROPERTIES, EnergyLevelConfig.REGULAR));
    public static final RegistryObject<Block> ADV_ENDER_STORAGE = BLOCKS.register("advanced_ender_storage", () -> new StorageBlock(BLOCK_PROPERTIES, EnergyLevelConfig.ADVANCED));

    // Items

    public static final RegistryObject<Item> ENDER_CELL = ITEMS.register("ender_cell", CellItem::new);
    public static final RegistryObject<Item> ENDER_GENERATOR_ITEM = ITEMS.register("ender_generator", () -> new GeneratorItem(ENDER_GENERATOR.get(), EnergyLevelConfig.REGULAR));
    public static final RegistryObject<Item> ADV_ENDER_GENERATOR_ITEM = ITEMS.register("advanced_ender_generator", () -> new GeneratorItem(ADV_ENDER_GENERATOR.get(), EnergyLevelConfig.ADVANCED));
    public static final RegistryObject<Item> ENDER_STORAGE_ITEM = ITEMS.register("ender_storage", () -> new StorageItem(ENDER_STORAGE.get(), EnergyLevelConfig.REGULAR));
    public static final RegistryObject<Item> ADV_ENDER_STORAGE_ITEM = ITEMS.register("advanced_ender_storage", () -> new StorageItem(ADV_ENDER_STORAGE.get(), EnergyLevelConfig.ADVANCED));

    // Tile Entities

    public static final RegistryObject<BlockEntityType<GeneratorEntity>> ENDER_GENERATOR_BE = BLOCK_ENTITIES.register("ender_generator", () -> BlockEntityType.Builder.of((pos, state) -> new GeneratorEntity(pos, state, EnergyLevelConfig.REGULAR), ENDER_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<GeneratorEntity>> ADV_ENDER_GENERATOR_BE = BLOCK_ENTITIES.register("advanced_ender_generator", () -> BlockEntityType.Builder.of((pos, state) -> new GeneratorEntity(pos, state, EnergyLevelConfig.ADVANCED), ADV_ENDER_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<StorageEntity>> ENDER_STORAGE_BE = BLOCK_ENTITIES.register("ender_storage", () -> BlockEntityType.Builder.of((pos, state) -> new StorageEntity(pos, state, EnergyLevelConfig.REGULAR), ENDER_STORAGE.get()).build(null));
    public static final RegistryObject<BlockEntityType<StorageEntity>> ADV_ENDER_STORAGE_BE = BLOCK_ENTITIES.register("advanced_ender_storage", () -> BlockEntityType.Builder.of((pos, state) -> new StorageEntity(pos, state, EnergyLevelConfig.ADVANCED), ADV_ENDER_STORAGE.get()).build(null));

    // Containers

    public static final RegistryObject<MenuType<GeneratorContainerMenu>> ENDER_GENERATOR_CONTAINER = CONTAINERS.register("ender_generator", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        //noinspection resource
        Level level = inv.player.level();
        GeneratorEntity te = (GeneratorEntity) level.getBlockEntity(pos);
        return new GeneratorContainerMenu(windowId, inv, te);
    }));
}
