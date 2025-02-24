package org.cyber.dev;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

public class TotemSwapMod implements ModInitializer, ClientModInitializer {
    private static final KeyBinding swapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.totemswap.swap",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_T,
            "category.totemswap"
    ));

    @Override
    public void onInitialize() {
        // No server-side initialization needed
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (swapKey.wasPressed()) {
                if (client.player != null) {
                    swapTotem(client.player);
                }
            }
        });
    }

    private static void swapTotem(PlayerEntity player) {
        if (player == null) return;

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                ItemStack offHandStack = player.getOffHandStack();
                player.getInventory().setStack(i, offHandStack);
                player.setStackInHand(Hand.OFF_HAND, stack);
                break;
            }
        }
    }
}
