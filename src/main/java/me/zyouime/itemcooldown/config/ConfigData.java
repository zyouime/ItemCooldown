package me.zyouime.itemcooldown.config;

import me.zyouime.itemcooldown.item.ItemCooldownStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigData {

    float scale = 2f;
    VPos verticalPos = VPos.TOP;
    List<ItemCooldownStatus> items = new ArrayList<>();

    public ConfigData() {

    }

    public void setField(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getField(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum VPos {
        TOP,
        CENTER,
        BOTTOM
    }

    public enum HPos {
        LEFT,
        MIDDLE,
        RIGHT
    }
}
