package me.zyouime.itemcooldown.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;

public interface CooldownRenderable {
    void tick();
    void render(DrawContext context);
    int getCooldown();
    Item getItem();
    void setCooldown(int cooldown);

    void updatePos(float x, float y);

}
