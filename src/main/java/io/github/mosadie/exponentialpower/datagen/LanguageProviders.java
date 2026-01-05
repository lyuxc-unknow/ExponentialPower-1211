package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LanguageProviders extends LanguageProvider {
    public LanguageProviders(PackOutput output) {
        super(output, ExponentialPower.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.exponentialpower","Exponential Power");
        add("screen.exponentialpower.generator_rate","Current Energy Generation:");
        add("screen.exponentialpower.storage_total_percent","Energy Percent : %s %%");
        add("screen.exponentialpower.storage_total_current","Energy Stored : %s FE");
        add("screen.exponentialpower.storage_total_max","Max Energy : %s FE");

        add("item.exponentialpower.ender_cell.tooltip1", "Materials used to make Ender Generator/Storage");
        add("item.exponentialpower.ender_cell.tooltip2", "Put it in (Mundane/Advanced) Ender Generator to increase power");
        add("item.exponentialpower.generator.tooltip.generated_max", "Max Generation : %s FE / tick");
        add("item.exponentialpower.generator.tooltip.output_max", "Max Output : %s FE / tick / direction");
        add("item.exponentialpower.generator.tooltip.transmission_range", "Remote Transmission Range : %s x %s x %s");
        add("item.exponentialpower.generator.tooltip.transmission_tips", "Remote transmission supports FE machines adjacent to the (Mundane/Advanced) Ender Storage");
        add("item.exponentialpower.storage.tooltip.stored_percent", "Energy Percent : %s %%");
        add("item.exponentialpower.storage.tooltip.stored_current", "Energy Stored : %s FE");
        add("item.exponentialpower.storage.tooltip.stored_max", "Max Energy : %s FE");

        add(Registration.ENDER_CELL.get(), "Ender Cell");
        add(Registration.ENDER_GENERATOR.get(), "Ender Generator");
        add(Registration.ENDER_STORAGE.get(), "Ender Storage");
        add(Registration.ADV_ENDER_GENERATOR.get(), "Advanced Ender Generator");
        add(Registration.ADV_ENDER_STORAGE.get(), "Advanced Ender Storage");
    }
}
