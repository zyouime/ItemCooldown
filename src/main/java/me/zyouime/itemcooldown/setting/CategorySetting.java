package me.zyouime.itemcooldown.setting;

import com.google.common.reflect.TypeToken;
import me.zyouime.itemcooldown.config.ConfigData;

public class CategorySetting extends Setting<ConfigData.Category> {

    public CategorySetting(String configKey) {
        super(configKey, new TypeToken<ConfigData.Category>() {}.getType());
    }

}
