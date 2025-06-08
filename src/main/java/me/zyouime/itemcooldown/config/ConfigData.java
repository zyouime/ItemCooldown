package me.zyouime.itemcooldown.config;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.util.items.CustomItemsNbt;
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
    Map<Category, List<AbstractItemCooldown>> items = new HashMap<>();
    @Expose
    Category selectedCategory = Category.HOLYWORLD;
    @Expose
    boolean enabled = true;
    @Expose
    boolean renderBackground = true;
    @Expose
    boolean alignment = true;
    @Expose
    float indent = 1.5f;

    public ConfigData() {
        items.put(Category.HOLYWORLD, fillHolyWorldItems());
        items.put(Category.FUNTIME, fillFunTimeItems());
        items.put(Category.CUSTOM, new ArrayList<>());
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

    private List<AbstractItemCooldown> fillHolyWorldItems() {
        List<AbstractItemCooldown> holyWorldItems = new ArrayList<>();
        holyWorldItems.add(VanillaItemCooldown.builder(Items.ENCHANTED_GOLDEN_APPLE).setPos(-50, -100).canUseWhenNoFightMode(true).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.GOLDEN_APPLE).setPos(-50, -70).setWhenNoFightMode(false).canUseWhenNoFightMode(true).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.CHORUS_FRUIT).setMaxCooldown(15).setPos(-25, -70).hasCustomCooldown(true).setWhenNoFightMode(false).canUseWhenNoFightMode(true).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.PRISMARINE_SHARD).setPos(-25, -100).resetWhenLeftTheServer(false).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.POPPED_CHORUS_FRUIT).setPos(0, -100).resetWhenLeftTheServer(false).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.NETHER_STAR).setPos(25, -100).resetWhenLeftTheServer(false).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.JACK_O_LANTERN).setPos(75, -100).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.SNOWBALL).setPos(50, -100).build());
        holyWorldItems.add(VanillaItemCooldown.builder(Items.ENDER_PEARL).setPos(0, -70).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.FIRE_CHARGE).setMaxCooldown(60).setPos(25, -70).setNbt(CustomItemsNbt.FIRE_CHARGE).setWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.POTION).setMaxCooldown(300).setPos(-25, -45).setNbt(CustomItemsNbt.WIN_POTION).setWhenNoFightMode(false).canUseWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.POTION).setMaxCooldown(150).setPos(50, -70).setNbt(CustomItemsNbt.LONG_TURTLE_MASTER).setWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.EXPERIENCE_BOTTLE).setMaxCooldown(600).setPos(-50, -40).setNbt(CustomItemsNbt.EXP_BOTTLE).setWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.FIREWORK_STAR).setMaxCooldown(30).setPos(0, -40).setNbt(CustomItemsNbt.FAREWELL_HUM).setWhenNoFightMode(true).build());
        holyWorldItems.add(CustomItemCooldown.builder(Items.POTION).setMaxCooldown(10).setPos(25, -40).setNbt(CustomItemsNbt.STRONG_HEALING_POTION).setWhenNoFightMode(true).build());
        return holyWorldItems;
    }

    private List<AbstractItemCooldown> fillFunTimeItems() {
        List<AbstractItemCooldown> funTimeItems = new ArrayList<>();
        funTimeItems.add(VanillaItemCooldown.builder(Items.FIRE_CHARGE).setPos(-50, -100).setWhenNoFightMode(false).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.NETHERITE_SCRAP).setPos(-50, -70).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.PHANTOM_MEMBRANE).setPos(-25, -100).build());
        funTimeItems.add(CustomItemCooldown.builder(Items.POTION).setPos(-25, -70).setMaxCooldown(20).resetWhenNoFightMode(true).setNbt(CustomItemsNbt.STRONG_HEALING_POTION).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.CHORUS_FRUIT).setPos(0, -100).resetWhenNoFightMode(true).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.GOLDEN_APPLE).setPos(25, -100).resetWhenNoFightMode(true).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.ENCHANTED_GOLDEN_APPLE).setPos(75, -100).resetWhenNoFightMode(true).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.SUGAR).setPos(50, -100).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.ENDER_EYE).setPos(0, -70).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.SNOWBALL).setPos(25, -70).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.ENDER_PEARL).setPos(25, -45).resetWhenNoFightMode(true).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.BOW).setPos(50, -70).resetWhenNoFightMode(true).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.CROSSBOW).setPos(-50, -45).resetWhenNoFightMode(true).build());
        funTimeItems.add(VanillaItemCooldown.builder(Items.TRIDENT).setPos(0, -45).resetWhenNoFightMode(true).build());
        return funTimeItems;
    }

    public enum Category {
        HOLYWORLD("HolyWorld"),
        FUNTIME("FunTime"),
        CUSTOM("Custom");
        public final String name;
        Category(String name) {
            this.name = name;
        }
    }
}
