package me.zyouime.itemcooldown.util;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.ItemCooldownScreen;
import me.zyouime.itemcooldown.screen.widget.element.CategoryElement;

import java.util.List;
import java.util.Map;

public interface Wrapper {

    ItemCooldown ic = ItemCooldown.getInstance();
    Map<String, List<AbstractItemCooldown>> cooldownItems = ic.cooldownItems();
    List<CategoryElement> categories = ic.getCategories();
}
