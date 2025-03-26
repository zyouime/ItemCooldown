package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.widget.CategoriesListWidget;
import me.zyouime.itemcooldown.screen.widget.element.CategoryElement;
import me.zyouime.itemcooldown.screen.widget.element.ItemElement;
import me.zyouime.itemcooldown.screen.widget.ItemsListWidget;
import me.zyouime.itemcooldown.util.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MainScreen extends Screen implements Wrapper {

    private final Screen parent;

    public MainScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        ItemsListWidget itemsList = new ItemsListWidget(client, this.width, this.height, 96, this.height - 64, 30);
        if (cooldownItems.get(ic.currentCategory) != null) {
            for (AbstractItemCooldown item : cooldownItems.get(ic.currentCategory)) {
                ItemElement element = new ItemElement(item, this);
                element.init();
                itemsList.addEntry(new ItemsListWidget.Elements(element));
            }
        }
        CategoriesListWidget categoriesList = new CategoriesListWidget(client, 80, this.height, 12, 96, 30, this);
        categoriesList.setLeftPos(width / 2 - 40);
        for (CategoryElement element : categories) {
            categoriesList.addEntry(new CategoriesListWidget.Elements(new CategoryElement(element.getCategory())));
        }
        ButtonWidget addItem = ButtonWidget.builder(Text.literal("Добавить"),
                press -> client.setScreen(new ItemCooldownScreen(this, null, ic.currentCategory)))
                .dimensions(width / 2 - 35, height - 40, 70, 20)
                .build();
        this.addDrawableChild(itemsList);
        this.addDrawableChild(addItem);
        this.addDrawableChild(categoriesList);
        super.init();
    }

    @Override
    public <T extends Element & Selectable> T addSelectableChild(T child) {
        return super.addSelectableChild(child);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    @Override
    public void clearAndInit() {
        super.clearAndInit();
    }

    public MinecraftClient getClient() {
        return this.client;
    }
}
