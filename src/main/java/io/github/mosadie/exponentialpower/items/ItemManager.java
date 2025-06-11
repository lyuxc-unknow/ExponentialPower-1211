package io.github.mosadie.exponentialpower.items;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;

public class ItemManager {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExponentialPower.MODID);

    public static void init() {
        CREATIVE_MODE_TABS.register("exponentialpower", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.exponentialpower"))
                .icon(() -> new ItemStack(Registration.ENDER_CELL.get()))
                .displayItems((parameters, output) -> {
                    output.accept(Registration.ENDER_CELL.get());
                    output.accept(Registration.ENDER_GENERATOR.get());
                    output.accept(Registration.ADV_ENDER_GENERATOR.get());
                    output.accept(Registration.ENDER_STORAGE.get());
                    output.accept(Registration.ADV_ENDER_STORAGE.get());
                }).build());
    }
}
