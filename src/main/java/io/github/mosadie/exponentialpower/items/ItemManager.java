package io.github.mosadie.exponentialpower.items;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ItemManager {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExponentialPower.MODID);

    private static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("exponentialpower", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.exponentialpower"))
            .icon(() -> new ItemStack(Registration.ENDER_CELL.get()))
            .displayItems((parameters, output) -> {
                output.accept(Registration.ENDER_CELL.get());
                output.accept(Registration.ENDER_GENERATOR_ITEM.get());
                output.accept(Registration.ADV_ENDER_GENERATOR_ITEM.get());
                output.accept(Registration.ENDER_STORAGE_ITEM.get());
                output.accept(Registration.ADV_ENDER_STORAGE_ITEM.get());
            })
            .build()
    );

    public static void init(FMLJavaModLoadingContext context) {
        CREATIVE_MODE_TABS.register(context.getModEventBus());
    }
}
