package me.zyouime.itemcooldown.screen.widget.element;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.EditItemCooldownScreen;
import me.zyouime.itemcooldown.screen.MainScreen;
import me.zyouime.itemcooldown.screen.widget.CustomButtonWidget;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemCustomElement implements CustomElement {

    private final AbstractItemCooldown item;
    private float x, y;
    private final MainScreen mainScreen;
    private CustomButtonWidget settingButton;
    private CustomButtonWidget deleteButton;
    private CustomButtonWidget visible;
    private final List<Drawable> widgets = new ArrayList<>();

    public ItemCustomElement(AbstractItemCooldown item, MainScreen mainScreen) {
        this.item = item;
        this.mainScreen = mainScreen;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        RenderHelper.drawCenteredYText(matrixStack, 28, y + 5, 0.9f, item.getItem().getName().getString(), Color.WHITE);
        RenderHelper.drawItem(matrixStack, item.getItem(), 7, y + 3.5f);
        widgets.forEach(widgets -> widgets.render(matrixStack, mouseX, mouseY, delta));
    }

    public void init() {
        MinecraftClient client = MinecraftClient.getInstance();
        settingButton = CustomButtonWidget.builder(Text.of("⚙"), press -> client.openScreen(new EditItemCooldownScreen(client.currentScreen, item))).dimensions(mainScreen.width - 170, 0, 20, 20).build();
        deleteButton = CustomButtonWidget.builder(Text.of("Удалить"), press -> {
            ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;
            settings.items.getValue().get(settings.selectedCategory.getValue()).remove(this.item);
            mainScreen.clearAndInit();
        }).dimensions(mainScreen.width - 70, 0, 60, 20).build();
        visible = CustomButtonWidget.builder(getItemVisible(), press -> {
            item.setVisible(!item.isVisible());
            press.setMessage(getItemVisible());
        }).dimensions(mainScreen.width - 140, 0, 60, 20).tooltip("Скрыть отображение этого предмета").build();
        this.addElement(settingButton);
        this.addElement(visible);
        this.addElement(deleteButton);
    }

    private <T extends Element & Drawable> void addElement(T element) {
        mainScreen.addChild(element);
        this.widgets.add(element);
    }

    private Text getItemVisible() {
        return Text.of(item.isVisible() ? "§fСкрыть" : "§cСкрыто");
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
