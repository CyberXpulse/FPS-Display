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
import net.minecraft.util.Hand;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class TotemSwapMod implements ModInitializer, ClientModInitializer {
private static final KeyBinding swapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
"key.totemswap.swap",
InputUtil.Type.KEYSYM,
GLFW.GLFW_KEY_T,
"category.totemswap"
));
public static final Identifier TOTEM_SWAP_PACKET_ID = new Identifier("totemswap", "swap");

@Override  
public void onInitialize() {  
    ServerPlayNetworking.registerGlobalReceiver(TOTEM_SWAP_PACKET_ID, (server, player, handler, buf, responseSender) -> {  
        server.execute(() -> {  
            swapTotem(player);  
        });  
    });  
}  

@Override  
public void onInitializeClient() {  
    ClientTickEvents.END_CLIENT_TICK.register(client -> {  
        while (swapKey.wasPressed()) {  
            if (client.player != null) {  
                PacketByteBuf buf = PacketByteBufs.create();  
                ClientPlayNetworking.send(TOTEM_SWAP_PACKET_ID, buf);  
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
            player.getInventory().markDirty();  
            break;  
        }  
    }  
}  
}

