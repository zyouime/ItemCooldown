package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class ItemCooldownScreen extends Screen {

    private final Screen parent;
    private final AbstractItemCooldown item;
    private float centerX, centerY;

    public ItemCooldownScreen(Screen parent, AbstractItemCooldown item, String category) {
        super(Text.empty());
        this.parent = parent;
        this.item = item;
    }

    @Override
    protected void init() {
        this.centerX = width / 2f;
        this.centerY = height / 2f;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        if (item == null) {
            float textScale = 1.5f;
            RenderHelper.drawCenteredXYText(context, centerX, 30, textScale, "Держите в активной руке предмет, который хотите добавить!!!", Color.WHITE);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
