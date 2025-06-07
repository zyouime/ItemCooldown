package me.zyouime.itemcooldown.util;

import net.minecraft.nbt.NbtCompound;

public class NbtHelper {

    public static void removeExtraKeys(NbtCompound compound) {
        compound.getKeys().removeIf(s -> s.equals("Damage")
                || s.equals("Enchantments")
                || s.equals("display")
                || s.equals("CustomModelData")
                || s.equals("Name")
                || s.equals("RepairCost")
                || s.equals("HideFlags")
                || s.equals("CustomPotionEffects"));
    }
}
