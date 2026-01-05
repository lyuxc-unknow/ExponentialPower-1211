package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStateProviders extends BlockStateProvider {
    public BlockStateProviders(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ExponentialPower.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // generation
        simpleBlock(Registration.ENDER_GENERATOR.get());
        simpleBlock(Registration.ADV_ENDER_GENERATOR.get());
        //storage
        simpleBlock(Registration.ENDER_STORAGE.get());
        simpleBlock(Registration.ADV_ENDER_STORAGE.get());
    }
}
