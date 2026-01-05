package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModelProviders extends ItemModelProvider {
    public ModelProviders(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExponentialPower.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // item
        basicItem(Registration.ENDER_CELL.getId());
        // generation
        withExistingParent(Registration.ENDER_GENERATOR.getId().getPath(),modLoc("block/ender_generator"));
        withExistingParent(Registration.ADV_ENDER_GENERATOR.getId().getPath(),modLoc("block/advanced_ender_generator"));
        // storage
        withExistingParent(Registration.ENDER_STORAGE.getId().getPath(),modLoc("block/ender_storage"));
        withExistingParent(Registration.ADV_ENDER_STORAGE.getId().getPath(),modLoc("block/advanced_ender_storage"));
    }
}
