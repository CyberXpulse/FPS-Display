package org.cyber.dev.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ConfirmSlotActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class TotemSwapMixin {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final KeyBinding swapKey = new KeyBinding("key.totemswap.swap",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_T, "category.totemswap");

    @Inject(method = "update", at = @At("HEAD"))
    private void onClientTick(CallbackInfo ci) {
        while (swapKey.wasPressed()) {
            swapTotem();
        }
    }

    private void swapTotem() {
        if (client.player == null || client.interactionManager == null || client.player.networkHandler == null) return;

        for (int i = 0; i < 36; i++) { // Check inventory
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                int slotId = i < 9 ? i + 36 : i; // Adjust for hotbar slots

                // Send inventory click packet
                client.player.networkHandler.sendPacket(new ClickSlotC2SPacket(
                        client.player.currentScreenHandler.syncId,
                        slotId,
                        40, // Offhand slot ID
                        SlotActionType.SWAP,
                        stack,
                        client.player.currentScreenHandler.getRevision()
                ));

                // Confirm slot change to prevent desync
                client.player.networkHandler.sendPacket(new ConfirmSlotActionC2SPacket(
                        client.player.currentScreenHandler.syncId,
                        slotId,
                        client.player.currentScreenHandler.getRevision(),
                        true
                ));

                // Send fake movement packet to register swap
                client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                        client.player.getX(), client.player.getY(), client.player.getZ(),
                        client.player.getYaw(), client.player.getPitch(), client.player.isOnGround()
                ));

                return;
            }
        }
    }
  }
