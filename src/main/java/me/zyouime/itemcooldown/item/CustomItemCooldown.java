package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.util.items.CustomItemsNbt;
import me.zyouime.itemcooldown.util.NbtHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

public class CustomItemCooldown extends AbstractItemCooldown {

    @Expose
    public NbtCompound nbt;
    @Expose
    private boolean dynamicNbt;

    private CustomItemCooldown(Builder builder) {
        super(builder.item, builder.maxCooldown, builder.x, builder.y, builder.resetWhenNoFightMode, builder.setWhenNoFightMode, builder.canUseWhenNoFightMode, builder.resetWhenLeftTheServer);
        try {
            this.nbt = StringNbtReader.parse(builder.nbt);
        } catch (Exception e) {
            System.err.println("залупа!: " + e.getMessage());
            this.nbt = null;
        }
        this.dynamicNbt = builder.dynamicNbt;

    }

    @Override
    public boolean shouldSetCooldown(ItemStack usedItem) {
        if (this.getCooldown() > 0) {
            return false;
        }
        if (this.nbt != null) {
            NbtCompound compound = usedItem.copy().getTag();
            if (compound == null) {
                return false;
            }
            NbtCompound itemNbt = this.nbt;
            NbtHelper.removeExtraKeys(compound);
            if (itemNbt.equals(compound) && !this.isDynamicNbt()) {
                boolean canSetCd = this.isSetWhenNoFightMode() || EventManager.isPvP();
                if (canSetCd) {
                    this.setCooldown(this.getMaxCooldown());
                    return true;
                }
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
        itemStack.setTag(this.nbt);
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
        private String nbt;
        private boolean resetWhenNoFightMode;
        private boolean setWhenNoFightMode;
        private boolean canUseWhenNoFightMode;
        private boolean dynamicNbt;
        private boolean resetWhenLeftTheServer = true;

        public Builder(ItemStack item) {
            this.item = item;
        }

        public Builder setMaxCooldown(int maxCooldown) {
            this.maxCooldown = maxCooldown;
            return this;
        }

        public Builder setPos(float x, float y) {
            this.x = x;
            this.y = y;
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

        public Builder resetWhenLeftTheServer(boolean resetWhenLeftTheServer) {
            this.resetWhenLeftTheServer = resetWhenLeftTheServer;
            return this;
        }

        public Builder setNbt(CustomItemsNbt nbt) {
            this.nbt = nbt.nbt;
            return this;
        }

        public Builder setNbt(String nbt) {
            this.nbt = nbt;
            return this;
        }

        public Builder resetWhenNoFightMode(boolean resetWhenNoFightMode) {
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
