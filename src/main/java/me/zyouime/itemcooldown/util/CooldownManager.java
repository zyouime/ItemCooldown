package me.zyouime.itemcooldown.util;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import static me.zyouime.itemcooldown.event.EventManager.isPvP;
import static me.zyouime.itemcooldown.util.Wrapper.cooldownItems;

public class CooldownManager implements Wrapper {

    public static void setCooldownIfNeeded(ItemStack usedItem) {
        if (cooldownItems.get(ic.currentCategory) == null) return;
        for (AbstractItemCooldown item : ic.cooldownItems().get(ic.currentCategory)) {
            item.shouldSetCooldown(usedItem);
        }
    }
}
