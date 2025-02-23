package org.cyber.dev;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class TotemSwapMod implements ClientModInitializer {
    private KeyBinding swapKey;

    @Override
    public void onInitializeClient() {
        swapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.totemswap.swap", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_T, 
                "category.totemswap"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (swapKey.wasPressed()) {
                swapTotem(client);
            }
        });
    }

    private void swapTotem(MinecraftClient client) {
        if (client.player == null || client.getNetworkHandler() == null) return;

        // If the offhand already has a totem, exit
        if (client.player.getInventory().offHand.get(0).getItem() == Items.TOTEM_OF_UNDYING) return;

        // Find a totem in the player's inventory
        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = client.player.getInventory().getStack(slot);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                sendSwapPacket(client, slot);
                break;
            }
        }
    }

    private void sendSwapPacket(MinecraftClient client, int slot) {
        int syncId = client.player.playerScreenHandler.syncId;

        ClickSlotC2SPacket packet = new ClickSlotC2SPacket(
                syncId, // Sync ID of the player's inventory
                0, // Not used in this context
                slot, // The slot containing the totem
                40, // The offhand slot ID
                SlotActionType.SWAP, // The action type (swap items)
                client.player.getInventory().getStack(slot), // The item being swapped
                client.player.currentScreenHandler.getRevision() // Inventory state tracking
        );

        client.getNetworkHandler().sendPacket(packet);
    }
            }
