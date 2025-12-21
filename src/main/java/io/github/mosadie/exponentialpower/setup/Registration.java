package io.github.mosadie.exponentialpower.setup;

import com.mojang.datafixers.DSL;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, ExponentialPower.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ExponentialPower.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExponentialPower.MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, ExponentialPower.MODID);

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CONTAINERS.register(modEventBus);
        modEventBus.addListener(Registration::registerCapabilities);
    }

    private static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(DyeColor.BLUE)
            .pushReaction(PushReaction.DESTROY)
            .strength(2.5f, 15.0f);

    // Blocks

    public static final DeferredHolder<Block, Block> ENDER_GENERATOR = BLOCKS.register("ender_generator", () -> new GeneratorBlock(BLOCK_PROPERTIES, EnergyLevelConfig.REGULAR));
    public static final DeferredHolder<Block, Block> ADV_ENDER_GENERATOR = BLOCKS.register("advanced_ender_generator", () -> new GeneratorBlock(BLOCK_PROPERTIES, EnergyLevelConfig.ADVANCED));
    public static final DeferredHolder<Block, Block> ENDER_STORAGE = BLOCKS.register("ender_storage", () -> new StorageBlock(BLOCK_PROPERTIES, EnergyLevelConfig.REGULAR));
    public static final DeferredHolder<Block, Block> ADV_ENDER_STORAGE = BLOCKS.register("advanced_ender_storage", () -> new StorageBlock(BLOCK_PROPERTIES, EnergyLevelConfig.ADVANCED));

    // Items

    public static final DeferredHolder<Item, Item> ENDER_CELL = ITEMS.register("ender_cell", CellItem::new);
    public static final DeferredHolder<Item, Item> ENDER_GENERATOR_ITEM = ITEMS.register("ender_generator", () -> new GeneratorItem(ENDER_GENERATOR.get(), EnergyLevelConfig.REGULAR));
    public static final DeferredHolder<Item, Item> ADV_ENDER_GENERATOR_ITEM = ITEMS.register("advanced_ender_generator", () -> new GeneratorItem(ADV_ENDER_GENERATOR.get(), EnergyLevelConfig.ADVANCED));
    public static final DeferredHolder<Item, Item> ENDER_STORAGE_ITEM = ITEMS.register("ender_storage", () -> new StorageItem(ENDER_STORAGE.get(), EnergyLevelConfig.REGULAR));
    public static final DeferredHolder<Item, Item> ADV_ENDER_STORAGE_ITEM = ITEMS.register("advanced_ender_storage", () -> new StorageItem(ADV_ENDER_STORAGE.get(), EnergyLevelConfig.ADVANCED));

    // Tile Entities

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorEntity>> ENDER_GENERATOR_BE = BLOCK_ENTITIES.register("ender_generator", () -> BlockEntityType.Builder.of((pos, state) -> new GeneratorEntity(pos, state, EnergyLevelConfig.REGULAR), ENDER_GENERATOR.get()).build(DSL.emptyPartType()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorEntity>> ADV_ENDER_GENERATOR_BE = BLOCK_ENTITIES.register("advanced_ender_generator", () -> BlockEntityType.Builder.of((pos, state) -> new GeneratorEntity(pos, state, EnergyLevelConfig.ADVANCED), ADV_ENDER_GENERATOR.get()).build(DSL.emptyPartType()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StorageEntity>> ENDER_STORAGE_BE = BLOCK_ENTITIES.register("ender_storage", () -> BlockEntityType.Builder.of((pos, state) -> new StorageEntity(pos, state, EnergyLevelConfig.REGULAR), ENDER_STORAGE.get()).build(DSL.emptyPartType()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StorageEntity>> ADV_ENDER_STORAGE_BE = BLOCK_ENTITIES.register("advanced_ender_storage", () -> BlockEntityType.Builder.of((pos, state) -> new StorageEntity(pos, state, EnergyLevelConfig.ADVANCED), ADV_ENDER_STORAGE.get()).build(DSL.emptyPartType()));

    // Containers

    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorContainerMenu>> ENDER_GENERATOR_CONTAINER = CONTAINERS.register("ender_generator", () -> IMenuTypeExtension.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        //noinspection resource
        Level level = inv.player.level();
        GeneratorEntity te = (GeneratorEntity) level.getBlockEntity(pos);
        return new GeneratorContainerMenu(windowId, inv, te);
    }));

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ENDER_GENERATOR_BE.get(),
                (entity, side) -> entity.getEnergyStorage()
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ADV_ENDER_GENERATOR_BE.get(),
                (entity, side) -> entity.getEnergyStorage()
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ENDER_STORAGE_BE.get(),
                StorageEntity::getEnergyStorage
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ADV_ENDER_STORAGE_BE.get(),
                StorageEntity::getEnergyStorage
        );
    }
}
