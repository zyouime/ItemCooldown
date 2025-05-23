package me.zyouime.itemcooldown.mixin;

import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.util.CooldownManager;
import me.zyouime.itemcooldown.util.UseItem;
import me.zyouime.itemcooldown.util.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements Wrapper {

    @Inject(method = "onCooldownUpdate", at = @At("RETURN"))
    private void onCooldownUpdate(CooldownUpdateS2CPacket packet, CallbackInfo ci) {
        int cooldown = packet.getCooldown();
        Item item = packet.getItem();
        if (cooldown > 0) {
            cooldownItems.get(ic.currentCategory).forEach(ct -> {
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

