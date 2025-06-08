package me.zyouime.itemcooldown.util;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class CooldownManager {

    public static void setCooldownIfNeeded(ItemStack usedItem) {
        Map<ConfigData.Category, List<AbstractItemCooldown>> items = ItemCooldown.getInstance().settings.items.getValue();
        ConfigData.Category selectedCategory = ItemCooldown.getInstance().settings.selectedCategory.getValue();
        if (items.get(selectedCategory) == null) {
            return;
        }
        for (AbstractItemCooldown item : items.get(selectedCategory)) {
            if (item.shouldSetCooldown(usedItem)) {
                return;
            }
        }
    }
}
