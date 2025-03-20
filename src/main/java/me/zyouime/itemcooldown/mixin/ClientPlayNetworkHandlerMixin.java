package me.zyouime.itemcooldown.mixin;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.ItemCooldownStatus;
import me.zyouime.itemcooldown.util.CooldownRenderable;
import me.zyouime.itemcooldown.util.UseItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Unique
    private final List<ItemCooldownStatus> cooldownItems = ItemCooldown.cooldownItems();

    @Inject(method = "onCooldownUpdate", at = @At("RETURN"))
    private void onCooldownUpdate(CooldownUpdateS2CPacket packet, CallbackInfo ci) {
        int cooldown = packet.getCooldown();
        Item item = packet.getItem();
        ItemCooldownStatus cooldownItem = new ItemCooldownStatus(item, cooldown);
        if (!cooldownItems.contains(cooldownItem)) {
            if (cooldown > 0) {
                cooldownItems.add(cooldownItem);
            }
        } else {
            if (cooldown > 0) {
                cooldownItems.forEach(ct -> {
                    if (ct.getCooldown() != cooldown && ct.getItem().equals(item)) {
                        ct.setCooldown(cooldown);
                    }
                });
            }
        }
    }

    @Inject(method = "onInventory", at = @At("RETURN"))
    private void onInventory(InventoryS2CPacket packet, CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player instanceof UseItem) {
            ItemStack itemStack = ((UseItem) player).getItem();
            if (itemStack != null) {
                boolean found = packet.getContents().stream().anyMatch(p -> ItemStack.areEqual(p, itemStack));

            }
        }
    }

}
