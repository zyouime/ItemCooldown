package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.util.HolyWorldItems;
import me.zyouime.itemcooldown.util.NbtManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

import static me.zyouime.itemcooldown.event.EventManager.isPvP;

public class CustomItemCooldown extends AbstractItemCooldown {

    @Expose
    public NbtCompound nbt;
    @Expose
    private boolean dynamicNbt;

    public CustomItemCooldown(ItemStack item, int maxCooldown, float x, float y, HolyWorldItems nbt, boolean resetWhenNoFightMode, boolean setWhenNoFightMode, boolean canUseWhenNoFightMode, boolean dynamicNbt) {
        super(item, maxCooldown, x, y, resetWhenNoFightMode, setWhenNoFightMode, canUseWhenNoFightMode);
        try {
            this.nbt = StringNbtReader.parse(nbt.nbt);
        } catch (Exception e) {
            System.err.println("залупа!: " + e.getMessage());
            this.nbt = null;
        }
        this.dynamicNbt = dynamicNbt;
    }

    public CustomItemCooldown(ItemStack item, int maxCooldown, float x, float y, HolyWorldItems nbt) {
        this(item, maxCooldown, x, y, nbt, false, true, false, false);
    }

    public CustomItemCooldown(ItemStack item, int maxCooldown, float x, float y, HolyWorldItems nbt, boolean setWhenNoFightMode, boolean canUseWhenNoFightMode) {
        this(item, maxCooldown, x, y, nbt, false, setWhenNoFightMode, canUseWhenNoFightMode, false);
    }

    @Override
    public void shouldSetCooldown(ItemStack usedItem) {
        if (this.getCooldown() > 0) return;
        if (usedItem.getNbt() != null && this.nbt != null) {
            NbtCompound compound = usedItem.copy().getNbt();
            NbtCompound itemNbt = this.nbt;
            NbtManager.prepareKeys(compound, itemNbt);
            if (itemNbt.equals(compound) && !this.isDynamicNbt()) {
                setCooldown(this);
            } else if (this.isDynamicNbt()) {
                for (String s : compound.getKeys()) {
                    if (itemNbt.contains(s) && !compound.getCompound(s).isEmpty()) {
                        setCooldown(this);
                        return;
                    }
                }
            }
        }
    }

    private void setCooldown(CustomItemCooldown cic) {
        if (!cic.isSetWhenNoFightMode()) {
            if (isPvP()) {
                cic.setCooldown(cic.getMaxCooldown());
            }
        } else {
            cic.setCooldown(cic.getMaxCooldown());
        }
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = super.getItem();
        itemStack.setNbt(this.nbt);
        return itemStack;
    }

    public boolean isDynamicNbt() {
        return dynamicNbt;
    }
}
