package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DataItems extends ItemModelProvider {

    public DataItems(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), ExponentialPower.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(Registration.ENDER_CELL.get().toString(), ResourceLocation.parse("item/handheld"), "layer0", ResourceLocation.fromNamespaceAndPath(ExponentialPower.MODID, "item/ender_cell"));
        withExistingParent(Registration.ENDER_GENERATOR_ITEM.get().toString(), ResourceLocation.fromNamespaceAndPath(ExponentialPower.MODID, "block/ender_generator"));
        withExistingParent(Registration.ADV_ENDER_GENERATOR_ITEM.get().toString(), ResourceLocation.fromNamespaceAndPath(ExponentialPower.MODID, "block/advanced_ender_generator"));
        withExistingParent(Registration.ENDER_STORAGE_ITEM.get().toString(), ResourceLocation.fromNamespaceAndPath(ExponentialPower.MODID, "block/ender_storage"));
        withExistingParent(Registration.ADV_ENDER_STORAGE_ITEM.get().toString(), ResourceLocation.fromNamespaceAndPath(ExponentialPower.MODID, "block/advanced_ender_storage"));
    }
}