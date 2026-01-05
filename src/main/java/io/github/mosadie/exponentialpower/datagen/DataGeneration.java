package io.github.mosadie.exponentialpower.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DataGeneration {
    public static void generation(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            generator.addProvider(true,new BlockStateProviders(packOutput,existingFileHelper));
            generator.addProvider(true,new ModelProviders(packOutput,existingFileHelper));
            generator.addProvider(true, new LootTableProvider(packOutput, Set.of(),
                    List.of(new LootTableProvider.SubProviderEntry(BlockLootTableProviders::new, LootContextParamSets.BLOCK)), lookupProvider));
            generator.addProvider(event.includeClient(), new BlockTagProviders(packOutput, lookupProvider, event.getExistingFileHelper()));
            generator.addProvider(event.includeServer(), new LanguageProviders(packOutput));
            generator.addProvider(true, new ModRecipeProvider(packOutput, lookupProvider));
        }
    }
}
