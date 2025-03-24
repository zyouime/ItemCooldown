package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.widget.ListElement;
import me.zyouime.itemcooldown.screen.widget.ListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MainScreen extends Screen {

    private final ItemCooldown ic = ItemCooldown.getInstance();

    public MainScreen(Screen parent) {
        super(Text.empty());
    }

    @Override
    protected void init() {

        ListWidget listWidget = new ListWidget(client, this.width, this.height, 48, this.height - 64, 30);
        for (AbstractItemCooldown item : ic.cooldownItems().get(ic.currentCategory)) {
            ListElement element = new ListElement(item, this);
            element.init();
            listWidget.addEntry(new ListWidget.Elements(element));
        }

        this.addDrawableChild(listWidget);
        super.init();
    }

    @Override
    public  <T extends Element & Selectable> T addSelectableChild(T child) {
        return super.addSelectableChild(child);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public MinecraftClient getClient() {
        return this.client;
    }
}
