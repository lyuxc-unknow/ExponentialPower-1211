package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagProviders extends BlockTagsProvider {
    public BlockTagProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ExponentialPower.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(Registration.ENDER_GENERATOR.get())
            .add(Registration.ENDER_STORAGE.get())
            .add(Registration.ADV_ENDER_GENERATOR.get())
            .add(Registration.ADV_ENDER_STORAGE.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .add(Registration.ADV_ENDER_GENERATOR.get())
            .add(Registration.ADV_ENDER_STORAGE.get());
    }
}
