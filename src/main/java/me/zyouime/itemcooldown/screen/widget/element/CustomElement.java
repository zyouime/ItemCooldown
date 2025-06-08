package me.zyouime.itemcooldown.screen.widget.element;

import net.minecraft.client.gui.DrawContext;

public interface CustomElement {
    void render(DrawContext context, int mouseX, int mouseY, float tickDelta);
    void updatePos(float x, float y);
}
