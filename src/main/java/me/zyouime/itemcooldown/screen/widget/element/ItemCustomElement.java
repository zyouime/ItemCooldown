package me.zyouime.itemcooldown.screen.widget.element;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.EditItemCooldownScreen;
import me.zyouime.itemcooldown.screen.MainScreen;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemCustomElement implements CustomElement {

    private final AbstractItemCooldown item;
    private float x, y;
    private final MainScreen mainScreen;
    private ButtonWidget settingButton;
    private ButtonWidget deleteButton;
    private ButtonWidget visible;
    private final List<Drawable> widgets = new ArrayList<>();

    public ItemCustomElement(AbstractItemCooldown item, MainScreen mainScreen) {
        this.item = item;
        this.mainScreen = mainScreen;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderHelper.drawCenteredYText(context, 28, y + 5, 0.9f, item.getItem().getName().getString(), Color.WHITE);
        RenderHelper.drawItem(context, item.getItem(), 7, y + 3.5f);
        widgets.forEach(widgets -> widgets.render(context, mouseX, mouseY, delta));
    }

    public void init() {
        MinecraftClient client = MinecraftClient.getInstance();
        settingButton = ButtonWidget.builder(Text.literal("⚙"), press ->
                        client.setScreen(new EditItemCooldownScreen(client.currentScreen, item)))
                .dimensions(mainScreen.width - 170, 0, 20, 20)
                .build();
        deleteButton = ButtonWidget.builder(Text.literal("Удалить"), press -> {
            ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;
            settings.items.getValue().get(settings.selectedCategory.getValue()).remove(this.item);
            mainScreen.clearAndInit();
        }).dimensions(mainScreen.width - 70, 0, 60, 20).build();
        visible = ButtonWidget.builder(getItemVisible(), press -> {
            item.setVisible(!item.isVisible());
            press.setMessage(getItemVisible());
        }).dimensions(mainScreen.width - 140, 0, 60, 20).build();
        visible.setTooltip(Tooltip.of(Text.literal("Скрыть отображение этого предмета")));
        this.addElement(settingButton);
        this.addElement(visible);
        this.addElement(deleteButton);
    }

    private <T extends Element & Selectable & Drawable> void addElement(T element) {
        mainScreen.addSelectableChild(element);
        this.widgets.add(element);
    }

    private Text getItemVisible() {
        return Text.literal(item.isVisible() ? "§fСкрыть" : "§cСкрыто");
    }

    @Override
    public void updatePos(float x, float y) {
        this.x = x;
        this.y = y;
        settingButton.setY((int) y + 3);
        deleteButton.setY((int) y + 3);
        visible.setY((int) y + 3);
    }
}
