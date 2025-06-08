package me.zyouime.itemcooldown.screen.widget.element;

import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class CategoryCustomElement implements CustomElement {

    private ConfigData.Category category;
    private float x, y;
    private Color color;

    public CategoryCustomElement(ConfigData.Category category) {
        this.category = category;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        RenderHelper.drawCenteredXYText(context, x + 40, y + 4, 0.9f, category.name, color == null ? Color.WHITE : color);
    }

    @Override
    public void updatePos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ConfigData.Category getCategory() {
        return category;
    }

    public void setCategory(ConfigData.Category category) {
        this.category = category;
    }
}
