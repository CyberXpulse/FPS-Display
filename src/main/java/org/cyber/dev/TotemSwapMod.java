package org.cyber.dev;

import net.fabricmc.api.ClientModInitializer; import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents; import net.minecraft.client.MinecraftClient; import net.minecraft.client.network.ClientPlayerEntity; import net.minecraft.entity.player.PlayerInventory; import net.minecraft.item.Items; import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket; import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket; import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket; import net.minecraft.screen.slot.SlotActionType; import net.minecraft.util.Hand; import net.minecraft.util.math.BlockPos; import org.lwjgl.glfw.GLFW;

public class TotemSwapMod implements ClientModInitializer { private static final MinecraftClient client = MinecraftClient.getInstance(); private static final int KEYBIND_TOTEM = GLFW.GLFW_KEY_T;

@Override
public void onInitializeClient() {
    ClientTickEvents.END_CLIENT_TICK.register(client -> {
        if (client.world == null || client.player == null) return;
        if (GLFW.glfwGetKey(client.getWindow().getHandle(), KEYBIND_TOTEM) == GLFW.GLFW_PRESS) {
            swapTotem();
        }
    });
}

private void swapTotem() {
    ClientPlayerEntity player = client.player;
    if (player == null) return;
    
    PlayerInventory inventory = player.getInventory();
    int totemSlot = findTotemSlot(inventory);
    if (totemSlot == -1) return;
    
    int offhandSlot = 45;
    
    // Open inventory packet simulation
    client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.OPEN_INVENTORY, BlockPos.ORIGIN, Hand.MAIN_HAND));
    
    // Simulate inventory click to swap totem
    client.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(player.currentScreenHandler.syncId, totemSlot, 0, SlotActionType.PICKUP, inventory.getStack(totemSlot), player.currentScreenHandler.getRevision()));
    client.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(player.currentScreenHandler.syncId, offhandSlot, 0, SlotActionType.PICKUP, inventory.getStack(offhandSlot), player.currentScreenHandler.getRevision()));
    
    // Close inventory to prevent flagging
    client.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(player.currentScreenHandler.syncId));
}

private int findTotemSlot(PlayerInventory inventory) {
    for (int i = 0; i < inventory.main.size(); i++) {
        if (inventory.getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
            return i;
        }
    }
    return -1;
}

}

