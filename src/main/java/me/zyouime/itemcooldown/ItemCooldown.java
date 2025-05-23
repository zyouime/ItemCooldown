package me.zyouime.itemcooldown;

import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.widget.element.CategoryElement;
import net.fabricmc.api.ModInitializer;

import java.util.*;

public class ItemCooldown implements ModInitializer {

    private static ItemCooldown instance;
    private Map<String, List<AbstractItemCooldown>> cooldownItems = new HashMap<>();
    private List<CategoryElement> categories = new ArrayList<>();
    public String currentCategory;

    public ItemCooldown() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        ModConfig.register();
        ConfigData configData = ModConfig.configData;
        cooldownItems = (Map<String, List<AbstractItemCooldown>>) configData.getField("items");
        categories = (List<CategoryElement>) configData.getField("categories");
        currentCategory = (String) configData.getField("selectedCategory");
        EventManager.registerEvents();
    }

    public Map<String, List<AbstractItemCooldown>> cooldownItems() {
        return this.cooldownItems;
    }

    public List<CategoryElement> getCategories() {
        return categories;
    }

    public static ItemCooldown getInstance() {
        return instance;
    }
}
