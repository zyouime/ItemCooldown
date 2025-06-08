package me.zyouime.itemcooldown.setting;

import com.google.common.reflect.TypeToken;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;

import java.util.List;
import java.util.Map;

public class ItemsSetting extends Setting<Map<ConfigData.Category, List<AbstractItemCooldown>>> {

    public ItemsSetting(String configKey) {
        super(configKey, new TypeToken<Map<ConfigData.Category, List<AbstractItemCooldown>>>() {}.getType());
    }
}
