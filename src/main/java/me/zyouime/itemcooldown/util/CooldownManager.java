package me.zyouime.itemcooldown.util;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import static me.zyouime.itemcooldown.event.EventManager.isPvP;
import static me.zyouime.itemcooldown.util.Wrapper.cooldownItems;

public class CooldownManager {

    public static void setCooldownIfNeeded(ItemCooldown ic, ItemStack usedItem) {
        if (cooldownItems.get(ic.currentCategory) == null) return;
        for (AbstractItemCooldown item : ic.cooldownItems().get(ic.currentCategory)) {
            if (item instanceof VanillaItemCooldown vic) {
                if (item.getItem().getItem().equals(usedItem.getItem())) {
                    if (vic.isHasCustomCooldown() && !vic.isSetWhenNoFightMode() && isPvP()) {
                        vic.setCooldown(vic.getMaxCooldown());
                    }
                    return;
                }
            }
            if (item.getCooldown() > 0) continue;
            if (item instanceof CustomItemCooldown cic) {
                if (usedItem.getNbt() != null && cic.nbt != null) {
                    NbtCompound compound = usedItem.copy().getNbt();
                    NbtCompound itemNbt = cic.nbt;
                    NbtManager.prepareKeys(compound, itemNbt);
                    if (itemNbt.equals(compound) && !cic.isDynamicNbt()) {
                        setCooldown(cic);
                        return;
                    } else if (cic.isDynamicNbt()) {
                        for (String s : compound.getKeys()) {
                            if (itemNbt.contains(s) && !compound.getCompound(s).isEmpty()) {
                                setCooldown(cic);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void setCooldown(CustomItemCooldown cic) {
        if (!cic.isSetWhenNoFightMode()) {
            if (isPvP()) {
                cic.setCooldown(cic.getMaxCooldown());
            }
        } else {
            cic.setCooldown(cic.getMaxCooldown());
        }
    }
}
