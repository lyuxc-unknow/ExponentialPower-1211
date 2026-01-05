package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BlockLootTableProviders extends BlockLootSubProvider {
    public static final Set<Block> BLOCKS = Set.of(
            Registration.ENDER_STORAGE.get(),
            Registration.ADV_ENDER_STORAGE.get(),
            Registration.ENDER_GENERATOR.get(),
            Registration.ADV_ENDER_GENERATOR.get()
    );

    protected BlockLootTableProviders(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        this.dropSelf(Registration.ENDER_STORAGE.get());
        this.dropSelf(Registration.ADV_ENDER_STORAGE.get());
        this.dropSelf(Registration.ENDER_GENERATOR.get());
        this.dropSelf(Registration.ADV_ENDER_GENERATOR.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BLOCKS;
    }
}
