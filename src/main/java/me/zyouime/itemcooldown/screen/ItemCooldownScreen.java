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

    public ItemCooldownScreen(Screen parent, AbstractItemCooldown item, String category) {
        super(Text.empty());
        this.parent = parent;
        this.item = item;
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
