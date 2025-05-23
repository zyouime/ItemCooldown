package me.zyouime.itemcooldown.util;

import net.minecraft.nbt.NbtCompound;

public class NbtManager {

    public static void removeExtraKeys(NbtCompound compound) {
        compound.getKeys().removeIf(s -> s.equals("Damage") || s.equals("Enchantments") || s.equals("display") || s.equals("CustomModelData") || s.equals("Name") || s.equals("RepairCost"));
    }

    public static void prepareKeys(NbtCompound usedItemNbt, NbtCompound cooldownItem) {
        removeExtraKeys(cooldownItem);
        usedItemNbt.getKeys().removeIf(s -> !cooldownItem.contains(s));
    }
}
