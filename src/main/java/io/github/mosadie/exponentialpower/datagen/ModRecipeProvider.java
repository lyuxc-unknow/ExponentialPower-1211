package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, provider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ENDER_CELL.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('E', Items.ENDER_EYE)
                .define('P', Items.DARK_PRISMARINE)
                .define('N', Tags.Items.INGOTS_NETHERITE)
                .pattern("DPD")
                .pattern("NEN")
                .pattern("DPD")
                .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ENDER_GENERATOR.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('O', Tags.Items.OBSIDIANS)
                .define('E', Registration.ENDER_CELL.get())
                .define('N', Tags.Items.INGOTS_NETHERITE)
                .pattern("NOD")
                .pattern("OEO")
                .pattern("DON")
                .unlockedBy("has_ender_cell", has(Registration.ENDER_CELL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ENDER_STORAGE.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('O', Tags.Items.OBSIDIANS)
                .define('E', Registration.ENDER_CELL.get())
                .define('P', Items.DARK_PRISMARINE)
                .pattern("DOD")
                .pattern("PEP")
                .pattern("DOD")
                .unlockedBy("has_ender_cell", has(Registration.ENDER_CELL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ADV_ENDER_GENERATOR.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('G', Registration.ENDER_GENERATOR.get())
                .define('E', Registration.ENDER_CELL.get())
                .pattern("DGD")
                .pattern("GEG")
                .pattern("DGD")
                .unlockedBy("has_ender_generator", has(Registration.ENDER_GENERATOR.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ADV_ENDER_STORAGE.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('S', Registration.ENDER_STORAGE.get())
                .define('E', Registration.ENDER_CELL.get())
                .pattern("DSD")
                .pattern("SES")
                .pattern("DSD")
                .unlockedBy("has_ender_storage", has(Registration.ENDER_STORAGE.get()))
                .save(output);
    }
}
