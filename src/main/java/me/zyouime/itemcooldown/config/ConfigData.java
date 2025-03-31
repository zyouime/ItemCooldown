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
        List<AbstractItemCooldown> holyWorldItems = new ArrayList<>();
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), -50, -100, true));
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.PRISMARINE_SHARD), -25, -100, false));
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.POPPED_CHORUS_FRUIT), 0, -100, false));
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.NETHER_STAR), 25, -100, false));
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.JACK_O_LANTERN), 0, -100, false));
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.SNOWBALL), 50, -100, false));
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.GOLDEN_APPLE), -50, -70, true));
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.CHORUS_FRUIT), 15, -25, -70, true));
        holyWorldItems.add(new VanillaItemCooldown(new ItemStack(Items.ENDER_PEARL),0, -70, false));
        holyWorldItems.add(new CustomItemCooldown(new ItemStack(Items.FIRE_CHARGE), 60 , 25,  -70, HolyWorldItems.FIRE_CHARGE));
        holyWorldItems.add(new CustomItemCooldown(new ItemStack(Items.POTION), 150, 50, -70,  HolyWorldItems.LONG_TURTLE_MASTER));
        holyWorldItems.add(new CustomItemCooldown(new ItemStack(Items.EXPERIENCE_BOTTLE), 600, -50, -40, HolyWorldItems.EXP_BOTTLE));
        holyWorldItems.add(new CustomItemCooldown(new ItemStack(Items.POTION), 300, -25, -40, HolyWorldItems.WIN_POTION, false, true));
        holyWorldItems.add(new CustomItemCooldown(new ItemStack(Items.FIREWORK_STAR), 30, 0, -40, HolyWorldItems.FAREWELL_HUM));
        holyWorldItems.add(new CustomItemCooldown(new ItemStack(Items.POTION), 10, 25, -40, HolyWorldItems.STRONG_HEALING_POTION));
        holyWorldItems.add(new CustomItemCooldown(new ItemStack(Items.NETHERITE_SWORD), 46, 50, -40, HolyWorldItems.FURY_SWORD,false, true, false, true));
        items.put("HolyWorld", holyWorldItems);
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
}
