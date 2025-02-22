package org.cyber.dev;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;
import io.netty.buffer.Unpooled;

@Environment(EnvType.CLIENT)
public class TotemSwapMod implements ClientModInitializer {
    private static KeyBinding swapKey;

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
                if (client.player != null) {
                    swapTotem(client);
                }
            }
        });
    }

    private void swapTotem(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;

        // Check if the offhand already has a totem
        if (client.player.getInventory().offHand.get(0).getItem() == Items.TOTEM_OF_UNDYING) {
            return; // Already holding a totem
        }

        // Find a totem in the main inventory
        for (int i = 0; i < client.player.getInventory().main.size(); i++) {
            ItemStack stack = client.player.getInventory().main.get(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                sendInventoryPacket(client, i);
                break;
            }
        }
    }

    private void sendInventoryPacket(MinecraftClient client, int slotIndex) {
        if (client.player == null || client.getNetworkHandler() == null) return;

        int syncId = client.player.playerScreenHandler.syncId; // Get current container ID
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        // Fake a player inventory click to swap the totem into the offhand
        ClickSlotC2SPacket packet = new ClickSlotC2SPacket(
                syncId,
                0,
                slotIndex,
                40, // Offhand slot ID
                SlotActionType.SWAP,
                client.player.getInventory().getStack(slotIndex),
                client.player.currentScreenHandler.getRevision()
        );

        client.getNetworkHandler().sendPacket(packet);
    }
}
