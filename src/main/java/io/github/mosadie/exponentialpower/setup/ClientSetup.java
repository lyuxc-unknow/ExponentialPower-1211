package io.github.mosadie.exponentialpower.setup;

import io.github.mosadie.exponentialpower.client.gui.GeneratorContainerScreen;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientSetup {
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(Registration.ENDER_GENERATOR_CONTAINER.get(), GeneratorContainerScreen::new);
    }
}
