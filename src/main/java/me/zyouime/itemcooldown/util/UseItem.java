package me.zyouime.itemcooldown.util;

import net.minecraft.item.ItemStack;

public interface UseItem {
    ItemStack getItem();
    void clearItem();
    void setItem(ItemStack item);
    int getSlot();
}
