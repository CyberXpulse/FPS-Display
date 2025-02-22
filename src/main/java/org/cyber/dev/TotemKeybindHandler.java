package org.cyber.dev;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TotemKeybindHandler {
    private static KeyBinding totemKey;

    public static void register() {
        // Register the keybind (default: "T")
        totemKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.totemswap", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_T, 
            "category.totemswap"
        ));

        // Listen for key press events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (totemKey.wasPressed() && client.player != null) {
                TotemSwapMod.LOGGER.info("Totem Swap Key Pressed");
                client.player.sendMessage(net.minecraft.text.Text.of("Attempting to swap totem..."), true);
            }
        });
    }
}
