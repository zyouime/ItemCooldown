package me.zyouime.itemcooldown.config;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.screen.widget.element.CategoryElement;
import me.zyouime.itemcooldown.util.HolyWorldItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigData {

    @Expose
    float scale = 2f;
    @Expose
    Map<String, List<AbstractItemCooldown>> items = new HashMap<>();
    @Expose
    List<CategoryElement> categories = new ArrayList<>();
    @Expose
    String selectedCategory = "HolyWorld";

    public ConfigData() {
        categories.add(new CategoryElement("HolyWorld"));
        categories.add(new CategoryElement("FunTime"));
        categories.add(new CategoryElement("Custom"));
        items.put("HolyWorld", fillList());
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

    private List<AbstractItemCooldown> fillList() {
        List<AbstractItemCooldown> holyWorldItems = new ArrayList<>();
        holyWorldItems.add(VanillaItemCooldown.builder(Items.ENCHANTED_GOLDEN_APPLE).setX(-50).setY(-100).canUseWhenNoFightMode(true).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.GOLDEN_APPLE).setX(-50).setY(-70).canUseWhenNoFightMode(true).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.CHORUS_FRUIT).setMaxCooldown(15).setX(-25).setY(-70).canUseWhenNoFightMode(true).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.PRISMARINE_SHARD).setX(-25).setY(-100).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.POPPED_CHORUS_FRUIT).setX(0).setY(-100).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.NETHER_STAR).setX(25).setY(-100).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.JACK_O_LANTERN).setX(75).setY(-100).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.SNOWBALL).setX(50).setY(-100).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.ENDER_PEARL).setX(0).setY(-70).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.FIRE_CHARGE).setMaxCooldown(60).setX(25).setY(-70).setNbt(HolyWorldItems.FIRE_CHARGE).setSetWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.POTION).setMaxCooldown(300).setX(-25).setY(-45).setNbt(HolyWorldItems.WIN_POTION).setSetWhenNoFightMode(false).setCanUseWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.POTION).setMaxCooldown(150).setX(50).setY(-70).setNbt(HolyWorldItems.LONG_TURTLE_MASTER).setSetWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.EXPERIENCE_BOTTLE).setMaxCooldown(600).setX(-50).setY(-40).setNbt(HolyWorldItems.EXP_BOTTLE).setSetWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.FIREWORK_STAR).setMaxCooldown(30).setX(0).setY(-40).setNbt(HolyWorldItems.FAREWELL_HUM).setSetWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.POTION).setMaxCooldown(10).setX(25).setY(-40).setNbt(HolyWorldItems.STRONG_HEALING_POTION).setSetWhenNoFightMode(true).build());
        return holyWorldItems;
    }
}
