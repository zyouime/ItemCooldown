package me.zyouime.itemcooldown.mixin;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.util.CooldownManager;
import me.zyouime.itemcooldown.util.UseItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onCooldownUpdate", at = @At("RETURN"))
    private void onCooldownUpdate(CooldownUpdateS2CPacket packet, CallbackInfo ci) {
        int cooldown = packet.getCooldown();
        Item item = packet.getItem();
        if (cooldown > 0) {
            ItemCooldown itemCooldown = ItemCooldown.getInstance();
            itemCooldown.settings.items.getValue().get(itemCooldown.settings.selectedCategory.getValue()).forEach(ct -> {
                if (ct instanceof VanillaItemCooldown vic) {
                    if (vic.getItem().getItem().equals(item)) {
                        if (vic.getCooldown() < cooldown) vic.setCooldown(cooldown);
                    }
                }
            });
        }
    }

    @Inject(method = "onInventory", at = @At("RETURN"))
    private void onInventory(InventoryS2CPacket packet, CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer instanceof UseItem player) {
            ItemStack itemStack = player.getItem();
            if (itemStack != null) {
                boolean notUsed = packet.getContents().stream().anyMatch(p -> ItemStack.areEqual(p, itemStack));
                if (!notUsed) {
                    CooldownManager.setCooldownIfNeeded(itemStack);
                }
                player.clearItem();
            }
        }
    }

}

