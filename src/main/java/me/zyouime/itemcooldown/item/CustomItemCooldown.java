package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.util.ItemNbt;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

public class CustomItemCooldown extends AbstractItemCooldown {

    @Expose
    public NbtCompound nbt;

    public CustomItemCooldown(ItemStack item, int maxCooldown, float x, float y, ItemNbt nbt, boolean resetWhenNoFightMode, boolean setWhenNoFightMode, boolean canUseWhenNoFightMode) {
        super(item, maxCooldown, x, y, resetWhenNoFightMode, setWhenNoFightMode, canUseWhenNoFightMode);
        try {
            this.nbt = StringNbtReader.parse(nbt.nbt);
        } catch (Exception e) {
            System.err.println("залупа!: " + e.getMessage());
            this.nbt = null;
        }
    }

    public CustomItemCooldown(ItemStack item, int maxCooldown, float x, float y, ItemNbt nbt, boolean canUseWhenNoFightMode) {
        this(item, maxCooldown, x, y, nbt, false, true, canUseWhenNoFightMode);
    }

    public CustomItemCooldown(ItemStack item, int maxCooldown, float x, float y, ItemNbt nbt) {
        this(item, maxCooldown, x, y, nbt, false, true, false);
    }

    public CustomItemCooldown(ItemStack item, int maxCooldown, float x, float y, ItemNbt nbt, boolean setWhenNoFightMode, boolean canUseWhenNoFightMode) {
        this(item, maxCooldown, x, y, nbt, false, setWhenNoFightMode, canUseWhenNoFightMode);
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = super.getItem();
        itemStack.setNbt(this.nbt);
        return itemStack;
    }
}
