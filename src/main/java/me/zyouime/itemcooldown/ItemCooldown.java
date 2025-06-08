package me.zyouime.itemcooldown;

import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.setting.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.*;

public class ItemCooldown implements ModInitializer {

    private static ItemCooldown instance;
    public Settings settings;
    public final KeyBinding OPEN_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding("Открыть настройки", InputUtil.Type.KEYSYM, -1, "ItemCooldown"));

    public ItemCooldown() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        ModConfig.initialize();
        settings = new Settings();
        EventManager.registerEvents();
    }

    public static ItemCooldown getInstance() {
        return instance;
    }

    public static class Settings {

        public List<Setting<?>> settingsList = new ArrayList<>();
        public ItemsSetting items = register(new ItemsSetting("items"));
        public NumberSetting scale = register(new NumberSetting("scale"));
        public CategorySetting selectedCategory = register(new CategorySetting("selectedCategory"));
        public BooleanSetting enabled = register(new BooleanSetting("enabled"));
        public BooleanSetting renderBackground = register(new BooleanSetting("renderBackground"));
        public BooleanSetting alignment = register(new BooleanSetting("alignment"));
        public NumberSetting indent = register(new NumberSetting("indent"));

        private <T extends Setting<?>> T register(T t) {
            this.settingsList.add(t);
            return t;
        }
    }
}
