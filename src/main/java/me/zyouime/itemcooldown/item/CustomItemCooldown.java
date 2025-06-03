package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.util.items.HolyWorldItems;
import me.zyouime.itemcooldown.util.NbtManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

import static me.zyouime.itemcooldown.event.EventManager.isPvP;

public class CustomItemCooldown extends AbstractItemCooldown {

    @Expose
    public NbtCompound nbt;
    @Expose
    private boolean dynamicNbt;

    private CustomItemCooldown(Builder builder) {
        super(builder.item, builder.maxCooldown, builder.x, builder.y, builder.resetWhenNoFightMode, builder.setWhenNoFightMode, builder.canUseWhenNoFightMode);
        try {
            this.nbt = StringNbtReader.parse(builder.nbt.nbt);
        } catch (Exception e) {
            System.err.println("залупа!: " + e.getMessage());
            this.nbt = null;
        }
        this.dynamicNbt = builder.dynamicNbt;

    }

    @Override
    public boolean shouldSetCooldown(ItemStack usedItem) {
        if (this.getCooldown() > 0) return false;
        if (usedItem.getNbt() != null && this.nbt != null) {
            NbtCompound compound = usedItem.copy().getNbt();
            NbtCompound itemNbt = this.nbt;
            NbtManager.prepareKeys(compound, itemNbt);
            if (itemNbt.equals(compound) && !this.isDynamicNbt()) {
                this.setCooldown(this.getMaxCooldown());
                return true;
            } else if (this.isDynamicNbt()) {
                for (String s : compound.getKeys()) {
                    if (itemNbt.contains(s) && !compound.getCompound(s).isEmpty()) {
                        this.setCooldown(this.getMaxCooldown());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = super.getItem();
        itemStack.setNbt(this.nbt);
        return itemStack;
    }

    public static Builder builder(Item item) {
        return new Builder(new ItemStack(item));
    }

    public static class Builder {
        private final ItemStack item;
        private int maxCooldown;
        private float x;
        private float y;
        private HolyWorldItems nbt;
        private boolean resetWhenNoFightMode;
        private boolean setWhenNoFightMode;
        private boolean canUseWhenNoFightMode;
        private boolean dynamicNbt;

        public Builder(ItemStack item) {
            this.item = item;
        }

        public Builder setMaxCooldown(int maxCooldown) {
            this.maxCooldown = maxCooldown;
            return this;
        }

        public Builder setX(float x) {
            this.x = x;
            return this;
        }

        public Builder setY(float y) {
            this.y = y;
            return this;
        }

        public Builder setNbt(HolyWorldItems nbt) {
            this.nbt = nbt;
            return this;
        }

        public Builder setResetWhenNoFightMode(boolean resetWhenNoFightMode) {
            this.resetWhenNoFightMode = resetWhenNoFightMode;
            return this;
        }

        public Builder setWhenNoFightMode(boolean setWhenNoFightMode) {
            this.setWhenNoFightMode = setWhenNoFightMode;
            return this;
        }

        public Builder canUseWhenNoFightMode(boolean canUseWhenNoFightMode) {
            this.canUseWhenNoFightMode = canUseWhenNoFightMode;
            return this;
        }

        public Builder setDynamicNbt(boolean dynamicNbt) {
            this.dynamicNbt = dynamicNbt;
            return this;
        }

        public CustomItemCooldown build() {
            return new CustomItemCooldown(this);
        }

    }

    public boolean isDynamicNbt() {
        return dynamicNbt;
    }
}
