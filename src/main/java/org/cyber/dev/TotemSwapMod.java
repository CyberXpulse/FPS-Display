package org.cyber.dev;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TotemSwapMod implements ModInitializer {
    public static final String MOD_ID = "totemswap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("TotemSwapMod initialized!");
        TotemKeybindHandler.register();
    }
}
