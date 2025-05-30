package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.widget.CategoriesListWidget;
import me.zyouime.itemcooldown.screen.widget.element.CategoryElement;
import me.zyouime.itemcooldown.screen.widget.element.ItemElement;
import me.zyouime.itemcooldown.screen.widget.ItemsListWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

public class MainScreen extends Screen {

    private final Screen parent;

    public MainScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        Map<ConfigData.Category, List<AbstractItemCooldown>> items = ItemCooldown.getInstance().settings.items.getValue();
        ConfigData.Category category = ItemCooldown.getInstance().settings.selectedCategory.getValue();
        ItemsListWidget itemsList = new ItemsListWidget(client, this.width, this.height, 96, this.height - 64, 30);
        if (items.get(category) != null) {
            for (AbstractItemCooldown item : items.get(category)) {
                ItemElement element = new ItemElement(item, this);
                element.init();
                itemsList.addEntry(new ItemsListWidget.Elements(element));
            }
        }
        CategoriesListWidget categoriesList = new CategoriesListWidget(client, 80, this.height, 12, 96, 30, this);
        categoriesList.setLeftPos(width / 2 - 40);
        for (CategoryElement element : ItemCooldown.getInstance().getCategories()) {
            categoriesList.addEntry(new CategoriesListWidget.Elements(new CategoryElement(element.getCategory())));
        }
        ButtonWidget addItem = ButtonWidget.builder(Text.literal("Добавить"),
                press -> client.setScreen(new ItemCooldownScreen(this, null, category)))
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
}
