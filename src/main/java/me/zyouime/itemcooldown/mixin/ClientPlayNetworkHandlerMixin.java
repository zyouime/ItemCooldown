package me.zyouime.itemcooldown.mixin;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.util.CooldownManager;
import me.zyouime.itemcooldown.util.UseItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
            ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;
            settings.items.getValue().get(settings.selectedCategory.getValue()).forEach(ct -> {
                if (ct instanceof VanillaItemCooldown vic) {
                    if (vic.getItem().getItem().equals(item)) {
                        if (vic.getCooldown() < cooldown) {
                            vic.setCooldown(cooldown);
                        }
                    }
                }
            });
        }
    }

    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("RETURN"))
    private void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        if (packet.getSyncId() != player.currentScreenHandler.syncId) {
            return;
        }
        int slot = packet.getSlot();
        if (slot < 36 || slot > 44) {
            return;
        }
        if (player instanceof UseItem clientPlayer) {
            ItemStack updatedItem = packet.getItemStack();
            ItemStack usedItem = clientPlayer.getItem();
            if (usedItem == null) {
                return;
            }
            if (clientPlayer.getSlot() != slot) {
                return;
            }
            if (isOver(updatedItem)) {
                CooldownManager.setCooldownIfNeeded(usedItem);
                clientPlayer.clearItem();
                return;
            }
            if (!ItemStack.areItemsEqual(usedItem, updatedItem)) {
                return;
            }
            NbtCompound usedItemNbt = usedItem.getNbt();
            NbtCompound updatedItemNbt = updatedItem.getNbt();
            if (updatedItemNbt == null || usedItemNbt == null) {
                return;
            }
            if (usedItemNbt.equals(updatedItemNbt) && updatedItem.getCount() + 1 == usedItem.getCount()) {
                CooldownManager.setCooldownIfNeeded(usedItem);
            }
            clientPlayer.clearItem();
        }
    }

    @Unique
    private boolean isOver(ItemStack updatedItem) {
        return updatedItem.isOf(Items.AIR) || updatedItem.isOf(Items.GLASS_BOTTLE);
    }
}

