package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DataRecipes extends RecipeProvider {

    public DataRecipes(DataGenerator generatorIn) {
        super(generatorIn.getPackOutput());
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ExponentialPower.LOGGER.info("Registering Recipes!");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ENDER_CELL.get())
                .pattern("DPD")
                .pattern("nIn")
                .pattern("DPD")
                .define('D', Blocks.DIAMOND_BLOCK)
                .define('n', Items.NETHERITE_INGOT)
                .define('P', Blocks.DARK_PRISMARINE)
                .define('I', Items.ENDER_EYE)
                .group("exponentialpower")
                .unlockedBy("enderEye", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_EYE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ENDER_GENERATOR.get())
                .pattern("nOD")
                .pattern("OcO")
                .pattern("DOn")
                .define('n', Items.NETHERITE_INGOT)
                .define('O', Tags.Items.OBSIDIAN)
                .define('D', Blocks.DIAMOND_BLOCK)
                .define('c', Registration.ENDER_CELL.get())
                .group("exponentialpower")
                .unlockedBy("endercell", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.ENDER_CELL.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ADV_ENDER_GENERATOR.get())
                .pattern("DGD")
                .pattern("GcG")
                .pattern("DGD")
                .define('D', Blocks.DIAMOND_BLOCK)
                .define('G', Registration.ENDER_GENERATOR.get())
                .define('c', Registration.ENDER_CELL.get())
                .group("exponentialpower")
                .unlockedBy("endergenerator", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.ENDER_GENERATOR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ENDER_STORAGE.get())
                .pattern("DOD")
                .pattern("PcP")
                .pattern("DOD")
                .define('D', Blocks.DIAMOND_BLOCK)
                .define('O', Tags.Items.OBSIDIAN)
                .define('P', Blocks.DARK_PRISMARINE)
                .define('c', Registration.ENDER_CELL.get())
                .group("exponentialpower")
                .unlockedBy("endercell", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.ENDER_CELL.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ADV_ENDER_STORAGE.get())
                .pattern("DSD")
                .pattern("ScS")
                .pattern("DSD")
                .define('D', Blocks.DIAMOND_BLOCK)
                .define('S', Registration.ENDER_STORAGE.get())
                .define('c', Registration.ENDER_CELL.get())
                .group("exponentialpower")
                .unlockedBy("enderstorage", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.ENDER_STORAGE.get()))
                .save(consumer);
    }
}