package me.zyouime.itemcooldown;

import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import net.fabricmc.api.ModInitializer;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCooldown implements ModInitializer {

    private static ItemCooldown instance;
    private Map<String, List<AbstractItemCooldown>> cooldownItems = new HashMap<>();
    public String currentCategory;

    public ItemCooldown() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        ModConfig.register();
        ConfigData configData = ModConfig.configData;
        cooldownItems = (Map<String, List<AbstractItemCooldown>>) configData.getField("items");
        currentCategory = (String) configData.getField("selectedCategory");
        EventManager.register();
    }

    public static void removeExtraKeys(NbtCompound removeNbt, NbtCompound cooldownItemNbt) {
        removeNbt.getKeys().removeIf(s -> !cooldownItemNbt.contains(s));
    }

    public Map<String, List<AbstractItemCooldown>> cooldownItems() {
        return this.cooldownItems;
    }

    public static ItemCooldown getInstance() {
        return instance;
    }
}
