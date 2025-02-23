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
                "key.totemswap.swap", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_T, "category.totemswap"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (swapKey.wasPressed()) swapTotem(client);
        });
    }

    private void swapTotem(MinecraftClient client) {
        if (client.player == null || client.getNetworkHandler() == null) return;

        // If already holding a totem, no need to swap
        if (client.player.getInventory().offHand.get(0).getItem() == Items.TOTEM_OF_UNDYING) return;

        // Find a totem in inventory
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
                syncId, 0, slot, 40, SlotActionType.SWAP, client.player.getInventory().getStack(slot),
                client.player.currentScreenHandler.getRevision()
        );
        client.getNetworkHandler().sendPacket(packet);
    }
            }
