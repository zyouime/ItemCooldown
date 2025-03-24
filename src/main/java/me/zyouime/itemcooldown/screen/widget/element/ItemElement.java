package me.zyouime.itemcooldown.screen.widget.element;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.ItemCooldownScreen;
import me.zyouime.itemcooldown.screen.MainScreen;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.awt.*;

public class ItemElement implements Element {

    private final AbstractItemCooldown item;
    private float x, y;
    private final MainScreen mainScreen;
    private ButtonWidget settingButton;
    private final ItemCooldown ic = ItemCooldown.getInstance();


    public ItemElement(AbstractItemCooldown item, MainScreen mainScreen) {
        this.item = item;
        this.mainScreen = mainScreen;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderHelper.drawCenteredYText(context, 28, y + 5, 0.9f, item.getItem().getName().getString(), Color.WHITE);
        RenderHelper.drawItem(context, item.getItem(), 7, y + 3.5f);
        settingButton.render(context, mouseX, mouseY, delta);
    }

    public void init() {
        MinecraftClient client = mainScreen.getClient();
        settingButton = ButtonWidget.builder(Text.literal("⚙"), press -> client.setScreen(new ItemCooldownScreen(client.currentScreen, item, ic.currentCategory))).dimensions(0, 0, 20, 20).build();
        mainScreen.addSelectableChild(settingButton);
    }

    @Override
    public void updatePos(float x, float y) {
        this.x = x;
        this.y = y;
        settingButton.setX(mainScreen.width - 30);
        settingButton.setY((int) y + 3);
    }
}
