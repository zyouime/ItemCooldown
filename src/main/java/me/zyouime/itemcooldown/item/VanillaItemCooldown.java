package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import net.minecraft.item.ItemStack;

import static me.zyouime.itemcooldown.event.EventManager.isPvP;

public class VanillaItemCooldown extends AbstractItemCooldown {

    @Expose
    private boolean hasCustomCooldown;

    public VanillaItemCooldown(ItemStack item, int maxCooldown, float x, float y, boolean hasCustomCooldown, boolean resetWhenNoFightMode, boolean setWhenNoFightMode, boolean canUseWhenNoFightMode) {
        super(item, maxCooldown, x, y, resetWhenNoFightMode, setWhenNoFightMode, canUseWhenNoFightMode);
        this.hasCustomCooldown = hasCustomCooldown;
    }

    public VanillaItemCooldown(ItemStack item, float x, float y, boolean canUseWhenNoFightMode) {
        this(item, 0, x, y, false, false, true, canUseWhenNoFightMode);
    }

    public VanillaItemCooldown(ItemStack item, int maxCooldown, float x, float y) {
        this(item, maxCooldown, x, y, true, false, false, true);
    }

    public VanillaItemCooldown(ItemStack item, int maxCooldown, float x, float y, boolean canUseWhenNoFightMode) {
        this(item, maxCooldown, x, y, true, false, false, canUseWhenNoFightMode);
    }

    @Override
    public void shouldSetCooldown(ItemStack usedItem) {
        if (this.getItem().getItem().equals(usedItem.getItem())) {
            if (this.isHasCustomCooldown() && !this.isSetWhenNoFightMode() && isPvP()) {
                this.setCooldown(this.getMaxCooldown());
            }
        }
    }

    public boolean isHasCustomCooldown() {
        return hasCustomCooldown;
    }
}
