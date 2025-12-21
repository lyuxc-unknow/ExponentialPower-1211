package io.github.mosadie.exponentialpower;

import io.github.mosadie.exponentialpower.items.ItemManager;
import io.github.mosadie.exponentialpower.setup.ClientSetup;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExponentialPower.MODID)
public class ExponentialPower {
    public static final String MODID = "exponentialpower";
    public static final Logger LOGGER = LogManager.getLogger();

    public ExponentialPower(ModContainer modContainer, IEventBus modEventBus) {
        Config.init(modContainer);
        Registration.init(modEventBus);
        modEventBus.addListener(ClientSetup::registerScreens);
        ItemManager.init(modEventBus);
    }
}
