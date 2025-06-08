package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.widget.CategoriesListWidget;
import me.zyouime.itemcooldown.screen.widget.element.CategoryCustomElement;
import me.zyouime.itemcooldown.screen.widget.element.ItemCustomElement;
import me.zyouime.itemcooldown.screen.widget.ItemsListWidget;
import me.zyouime.itemcooldown.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;
import java.util.Map;

public class MainScreen extends Screen {

    private final Screen parent;
    private final List<CategoryCustomElement> categories = List.of(
            new CategoryCustomElement(ConfigData.Category.HOLYWORLD),
            new CategoryCustomElement(ConfigData.Category.FUNTIME),
            new CategoryCustomElement(ConfigData.Category.CUSTOM)
    );
    private final ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;
    private ItemsListWidget itemsList;
    private final Identifier TELEGRAM = new Identifier("itemcooldown", "telegram.png");

    public MainScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        Map<ConfigData.Category, List<AbstractItemCooldown>> items = settings.items.getValue();
        ConfigData.Category category = settings.selectedCategory.getValue();
        itemsList = new ItemsListWidget(client, width, height, 96, height - 80, 30);
        if (items.get(category) != null) {
            for (AbstractItemCooldown item : items.get(category)) {
                ItemCustomElement element = new ItemCustomElement(item, this);
                element.init();
                itemsList.addEntry(new ItemsListWidget.Elements(element));
            }
        }
        CategoriesListWidget categoriesList = new CategoriesListWidget(client, 80, height, 12, 96, 30, this);
        categoriesList.setLeftPos(width / 2 - 40);
        for (CategoryCustomElement element : categories) {
            categoriesList.addEntry(new CategoriesListWidget.Elements(new CategoryCustomElement(element.getCategory())));
        }
        ButtonWidget addItem = ButtonWidget.builder(Text.literal("Добавить предмет"), press ->
                        client.setScreen(new CreateItemCooldownScreen(this)))
                .dimensions(width / 2 - 60, height - 65, 120, 20)
                .build();
        ButtonWidget saveAndExit = ButtonWidget.builder(Text.literal("Сохранить и выйти"), press -> this.close()).dimensions(width / 2 - 60, height - 35, 120, 20).build();
        ButtonWidget positionScreen = ButtonWidget.builder(Text.literal("Настроить отображение"), press ->
                        client.setScreen(new PositionScreen(this)))
                .dimensions(10, height - 65, 140, 20)
                .build();
        ButtonWidget modEnabled = ButtonWidget.builder(PositionScreen.getButtonMessage("Включить мод", settings.enabled), press -> {
            settings.enabled.setValue(!settings.enabled.getValue());
            press.setMessage(PositionScreen.getButtonMessage("Включить мод", settings.enabled));
        }).dimensions(20, height - 35, 120, 20).build();
        this.addDrawableChild(itemsList);
        this.addDrawableChild(addItem);
        this.addDrawableChild(categoriesList);
        this.addDrawableChild(saveAndExit);
        this.addDrawableChild(positionScreen);
        this.addDrawableChild(modEnabled);
        super.init();
    }

    @Override
    public <T extends Element & Selectable> T addSelectableChild(T child) {
        return super.addSelectableChild(child);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        context.drawTexture(TELEGRAM, width - 37, height - 37, 27, 27, 0, 0, 128, 128, 128, 128);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        settings.settingsList.forEach(Setting::save);
        client.setScreen(parent);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= width - 37 && mouseX <= width - 10 && mouseY >= height - 37 && mouseY <= height - 10) {
            client.setScreen(new ConfirmLinkScreen(isConfirmed -> {
                if (isConfirmed) {
                    Util.getOperatingSystem().open("https://t.me/zyouime13");
                } else {
                    client.setScreen(this);
                }
            }, Text.literal("§bЗмееныш13"),"https://t.me/zyouime13",true));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void clearAndInit() {
        double scroll = itemsList.getScrollAmount();
        this.clearChildren();
        GuiNavigationPath guiNavigationPath = this.getFocusedPath();
        if (guiNavigationPath != null) {
            guiNavigationPath.setFocused(false);
        }
        this.init();
        itemsList.setScrollAmount(scroll);
    }
}
