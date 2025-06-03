package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.widget.CategoriesListWidget;
import me.zyouime.itemcooldown.screen.widget.CustomSliderWidget;
import me.zyouime.itemcooldown.screen.widget.element.CategoryElement;
import me.zyouime.itemcooldown.screen.widget.element.ItemElement;
import me.zyouime.itemcooldown.screen.widget.ItemsListWidget;
import me.zyouime.itemcooldown.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

public class MainScreen extends Screen {

    private final Screen parent;
    private final List<CategoryElement> categories = List.of(
            new CategoryElement(ConfigData.Category.HOLYWORLD),
            new CategoryElement(ConfigData.Category.FUNTIME),
            new CategoryElement(ConfigData.Category.CUSTOM)
    );

    public MainScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        Map<ConfigData.Category, List<AbstractItemCooldown>> items = ItemCooldown.getInstance().settings.items.getValue();
        ConfigData.Category category = ItemCooldown.getInstance().settings.selectedCategory.getValue();
        ItemsListWidget itemsList = new ItemsListWidget(client, width, height, 96, height - 80, 30);
        if (items.get(category) != null) {
            for (AbstractItemCooldown item : items.get(category)) {
                ItemElement element = new ItemElement(item, this);
                element.init();
                itemsList.addEntry(new ItemsListWidget.Elements(element));
            }
        }
        CustomSliderWidget sliderWidget = new CustomSliderWidget(20, height - 30, 120, 20, Text.of("Размеры иконок: "), 1, 3, ItemCooldown.getInstance().settings.scale);
        CategoriesListWidget categoriesList = new CategoriesListWidget(client, 80, height, 12, 96, 30, this);
        categoriesList.setLeftPos(width / 2 - 40);
        for (CategoryElement element : categories) {
            categoriesList.addEntry(new CategoriesListWidget.Elements(new CategoryElement(element.getCategory())));
        }
        ButtonWidget addItem = ButtonWidget.builder(Text.literal("Добавить"), press ->
                        client.setScreen(new ItemCooldownScreen(this, null, category)))
                .dimensions(width / 2 - 35, height - 60, 70, 20)
                .build();
        ButtonWidget positionScreen = ButtonWidget.builder(Text.literal("Настроить положение"), press ->
                        client.setScreen(new PositionScreen(this)))
                .dimensions(20, height - 60, 120, 20)
                .build();
        this.addDrawableChild(itemsList);
        this.addDrawableChild(addItem);
        this.addDrawableChild(categoriesList);
        this.addDrawableChild(sliderWidget);
        this.addDrawableChild(positionScreen);
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
        ItemCooldown.getInstance().settings.settingsList.forEach(Setting::save);
        client.setScreen(parent);
    }

    @Override
    public void clearAndInit() {
        super.clearAndInit();
    }
}
