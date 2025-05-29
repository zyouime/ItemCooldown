package me.zyouime.itemcooldown;

import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.screen.widget.element.CategoryElement;
import me.zyouime.itemcooldown.setting.CategorySetting;
import me.zyouime.itemcooldown.setting.ItemsSetting;
import me.zyouime.itemcooldown.setting.NumberSetting;
import me.zyouime.itemcooldown.setting.Setting;
import net.fabricmc.api.ModInitializer;

import java.util.*;

public class ItemCooldown implements ModInitializer {

    private static ItemCooldown instance;
    private final List<CategoryElement> categories = List.of(
            new CategoryElement(ConfigData.Category.HOLYWORLD),
            new CategoryElement(ConfigData.Category.FUNTIME),
            new CategoryElement(ConfigData.Category.CUSTOM)
    );
    public Settings settings;

    public ItemCooldown() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        ModConfig.initialize();
        settings = new Settings();
        EventManager.registerEvents();
    }

    public List<CategoryElement> getCategories() {
        return new ArrayList<>(categories);
    }

    public static ItemCooldown getInstance() {
        return instance;
    }

    public static class Settings {

        public List<Setting<?>> settingsList = new ArrayList<>();
        public ItemsSetting items = register(new ItemsSetting("items"));
        public NumberSetting scale = register(new NumberSetting("scale"));
        public CategorySetting selectedCategory = register(new CategorySetting("selectedCategory"));

        private <T extends Setting<?>> T register(T t) {
            this.settingsList.add(t);
            return t;
        }
    }
}
