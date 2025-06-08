package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static me.zyouime.itemcooldown.event.EventManager.isPvP;

public class VanillaItemCooldown extends AbstractItemCooldown {

    @Expose
    private boolean hasCustomCooldown;

    private VanillaItemCooldown(Builder builder) {
        super(builder.item, builder.maxCooldown, builder.x, builder.y, builder.resetWhenNoFightMode, builder.setWhenNoFightMode, builder.canUseWhenNoFightMode, builder.resetWhenLeftTheServer);
        this.hasCustomCooldown = builder.hasCustomCooldown;
    }

    @Override
    public boolean shouldSetCooldown(ItemStack usedItem) {
        if (this.getItem().getItem().equals(usedItem.getItem())) {
            if (this.isHasCustomCooldown() && !this.isSetWhenNoFightMode() && isPvP()) {
                this.setCooldown(this.getMaxCooldown());
                return true;
            }
        }
        return false;
    }

    public boolean isHasCustomCooldown() {
        return hasCustomCooldown;
    }

    public void hasCustomCooldown(boolean hasCustomCooldown) {
        this.hasCustomCooldown = hasCustomCooldown;
    }

    public static Builder builder(Item item) {
        return new Builder(new ItemStack(item));
    }

    public static class Builder {
        private final ItemStack item;
        private int maxCooldown;
        private float x;
        private float y;
        private boolean hasCustomCooldown;
        private boolean resetWhenNoFightMode;
        //если есть кастом кулдаун помимо ванилки, тогда этот параметр влияет
        private boolean setWhenNoFightMode = true;
        private boolean canUseWhenNoFightMode;
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

        public Builder hasCustomCooldown(boolean hasCustomCooldown) {
            this.hasCustomCooldown = hasCustomCooldown;
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

        public VanillaItemCooldown build() {
            return new VanillaItemCooldown(this);
        }
    }
}
