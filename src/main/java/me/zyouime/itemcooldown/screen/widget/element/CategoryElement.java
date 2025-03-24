package me.zyouime.itemcooldown.screen.widget.element;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class CategoryElement implements Element {

    @Expose
    private String category;
    private float x, y;
    private Color color;

    public CategoryElement(String category) {
        this.category = category;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        RenderHelper.drawCenteredXYText(context, x + 43, y + 4, 0.9f, category, color == null ? Color.WHITE : color);
    }

    @Override
    public void updatePos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
