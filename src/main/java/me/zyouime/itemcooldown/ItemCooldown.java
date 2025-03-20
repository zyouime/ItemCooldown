package me.zyouime.itemcooldown;

import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.item.ItemCooldownStatus;
import me.zyouime.itemcooldown.util.CooldownRenderable;
import net.fabricmc.api.ModInitializer;

import java.util.ArrayList;
import java.util.List;

public class ItemCooldown implements ModInitializer {
    /**
     * Runs the mod initializer.
     */

    private static List<ItemCooldownStatus> cooldownItems = new ArrayList<>();

    @Override
    public void onInitialize() {
        ModConfig.register();
        cooldownItems = (List<ItemCooldownStatus>) ModConfig.configData.getField("items");
        EventManager.register();
    }

    public static List<ItemCooldownStatus> cooldownItems() {
        return ItemCooldown.cooldownItems;
    }
}
