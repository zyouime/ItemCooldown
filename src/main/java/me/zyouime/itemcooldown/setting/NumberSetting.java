package me.zyouime.itemcooldown.setting;

import com.google.common.reflect.TypeToken;

public class NumberSetting extends Setting<Float> {

    public NumberSetting(String configKey) {
        super(configKey, new TypeToken<Float>() {}.getType());
    }

    public String toString() {
        return Float.toString(this.getValue());
    }
}
