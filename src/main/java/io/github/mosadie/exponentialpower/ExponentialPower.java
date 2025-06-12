package io.github.mosadie.exponentialpower;

import io.github.mosadie.exponentialpower.items.ItemManager;
import io.github.mosadie.exponentialpower.setup.ClientSetup;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExponentialPower.MODID)
public class ExponentialPower {
    public static final String MODID = "exponentialpower";
    public static final Logger LOGGER = LogManager.getLogger();

    public ExponentialPower(FMLJavaModLoadingContext context) {
        Config.init(context);
        Registration.init(context);
        IEventBus bus = context.getModEventBus();
        bus.addListener(ClientSetup::init);
        ItemManager.init(context);
    }
}
