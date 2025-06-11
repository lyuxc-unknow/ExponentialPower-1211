package io.github.mosadie.exponentialpower.datagen;

import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class DataLangChinese extends LanguageProvider {

    public DataLangChinese(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), ExponentialPower.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(Registration.ENDER_CELL, "末影单元");

        addBlock(Registration.ENDER_GENERATOR, "末影发电机");
        addBlock(Registration.ADV_ENDER_GENERATOR, "高级末影发电机");

        addBlock(Registration.ENDER_STORAGE, "末影能量电容");
        addBlock(Registration.ADV_ENDER_STORAGE, "高级末影能量电容");

        add("itemGroup.exponentialpower", "指数能源");

        add("screen.exponentialpower.generator_rate", "当前发电量:");
        add("screen.exponentialpower.storage_total", "当前储电量: %s/%s RF (%s%%)");

        add("item.exponentialpower.storage.tooltip.stored", "当前储电量:");
    }
}
