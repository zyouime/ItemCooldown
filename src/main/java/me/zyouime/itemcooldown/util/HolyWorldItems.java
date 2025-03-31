package me.zyouime.itemcooldown.util;

public enum HolyWorldItems {

    LONG_TURTLE_MASTER("{Potion:\"minecraft:long_turtle_master\"}"),
    FIRE_CHARGE("{kringeItems:{type:\"ExplosiveStuff\"}}"),
    WIN_POTION("{CustomPotionColor:33461,kringeItems:{type:\"win-potion\"}}"),
    STRONG_HEALING_POTION("{Potion:\"minecraft:strong_healing\"}"),
    FAREWELL_HUM("{pyrotechnic-item:{name:\"FAREWELL_HUM\"}}"),
    EXP_BOTTLE("{kringeItems:{type:\"ExpBottle\"}}"),
    JAKE_LUMINAIRE("{kringeItems:{type:\"JakeLuminaire\"}}"),
    FURY_SWORD("{explosive-ability:{}}");

    public final String nbt;

    HolyWorldItems(String nbt) {
        this.nbt = nbt;
    }
}
