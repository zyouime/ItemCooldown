package me.zyouime.itemcooldown.util;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class CooldownManager {

    private static final Map<ConfigData.Category, List<AbstractItemCooldown>> items = ItemCooldown.getInstance().settings.items.getValue();
    private static final ConfigData.Category selectedCategory = ItemCooldown.getInstance().settings.selectedCategory.getValue();

    public static void setCooldownIfNeeded(ItemStack usedItem) {
        if (items.get(selectedCategory) == null) return;
        for (AbstractItemCooldown item : items.get(selectedCategory)) {
            if (item.shouldSetCooldown(usedItem)) return;
        }
    }
}
