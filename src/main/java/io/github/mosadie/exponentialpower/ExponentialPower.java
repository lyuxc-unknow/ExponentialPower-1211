package io.github.mosadie.exponentialpower;

import io.github.mosadie.exponentialpower.items.ItemManager;
import io.github.mosadie.exponentialpower.setup.ClientSetup;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExponentialPower.MODID)
public class ExponentialPower {
    public static final String MODID = "exponentialpower";
    public static final Logger LOGGER = LogManager.getLogger();

    public ExponentialPower() {
        //noinspection removal
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        context.registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        Registration.init(context);
        context.getModEventBus().addListener(ClientSetup::init);
        ItemManager.init();
    }
}
