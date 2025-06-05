package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class EditItemCooldownScreen extends Screen {

    private final Screen parent;
    private final AbstractItemCooldown item;
    private float centerX, centerY;
    private final ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;

    public EditItemCooldownScreen(Screen parent, AbstractItemCooldown item) {
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
        RenderHelper.drawCenteredXYText(context, centerX, 30, 1.5f, "Редактирование предмета из категории " + settings.selectedCategory.getValue().name, Color.WHITE);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
