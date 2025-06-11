package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataLootTables extends LootTableProvider {
    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();

    public DataLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn.getPackOutput(),
                BuiltInLootTables.all(),
                List.of(new LootTableProvider.SubProviderEntry(VanillaFishingLoot::new, LootContextParamSets.FISHING),
                        new LootTableProvider.SubProviderEntry(VanillaChestLoot::new, LootContextParamSets.CHEST),
                        new LootTableProvider.SubProviderEntry(VanillaEntityLoot::new, LootContextParamSets.ENTITY),
                        new LootTableProvider.SubProviderEntry(VanillaBlockLoot::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(VanillaPiglinBarterLoot::new, LootContextParamSets.PIGLIN_BARTER),
                        new LootTableProvider.SubProviderEntry(VanillaGiftLoot::new, LootContextParamSets.GIFT),
                        new LootTableProvider.SubProviderEntry(VanillaArchaeologyLoot::new, LootContextParamSets.ARCHAEOLOGY)
                )
        );
    }

    protected void addTables() {
        ExponentialPower.LOGGER.info("Generating Loot Tables!");
        lootTables.put(Registration.ENDER_GENERATOR.get(), createEnderGeneratorTable("endergenerator", Registration.ENDER_GENERATOR.get(), Registration.ENDER_GENERATOR_BE.get()));
        lootTables.put(Registration.ADV_ENDER_GENERATOR.get(), createEnderGeneratorTable("advancedendergenerator", Registration.ADV_ENDER_GENERATOR.get(), Registration.ADV_ENDER_GENERATOR_BE.get()));

        lootTables.put(Registration.ENDER_STORAGE.get(), createEnderStorageTable("enderstorage", Registration.ENDER_STORAGE.get()));
        lootTables.put(Registration.ADV_ENDER_STORAGE.get(), createEnderStorageTable("advancedenderstorage", Registration.ADV_ENDER_STORAGE.get()));
    }

    protected LootTable.Builder createEnderGeneratorTable(String name, Block block, BlockEntityType<?> type) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("inv", "BlockEntityTag.inv", CopyNbtFunction.MergeStrategy.REPLACE)
                                .copy("energy", "BlockEntityTag.energy", CopyNbtFunction.MergeStrategy.REPLACE))
                        .apply(SetContainerContents.setContents(type)
                                .withEntry(DynamicLoot.dynamicEntry(ResourceLocation.fromNamespaceAndPath("minecraft", "contents"))))
                );
        return LootTable.lootTable().withPool(builder);
    }

    protected LootTable.Builder createEnderStorageTable(String name, Block block) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("energy", "BlockEntityTag.energy", CopyNbtFunction.MergeStrategy.REPLACE))
                );
        return LootTable.lootTable().withPool(builder);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput p_254060_) {
        addTables();
        return super.run(p_254060_);
    }
}
