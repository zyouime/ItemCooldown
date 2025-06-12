package me.zyouime.itemcooldown.screen.widget.element;

import net.minecraft.client.util.math.MatrixStack;

public interface CustomElement {
    void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta);
    void updatePos(float x, float y);
}
