package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.widget.CategoriesListWidget;
import me.zyouime.itemcooldown.screen.widget.CustomButtonWidget;
import me.zyouime.itemcooldown.screen.widget.element.ItemCustomElement;
import me.zyouime.itemcooldown.screen.widget.ItemsListWidget;
import me.zyouime.itemcooldown.setting.Setting;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.*;

public class MainScreen extends Screen {

    private final Screen parent;
    private final List<ConfigData.Category> categories = Arrays.asList(
            ConfigData.Category.HOLYWORLD,
            ConfigData.Category.FUNTIME,
            ConfigData.Category.CUSTOM
    );
    private final ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;
    private ItemsListWidget itemsList;
    private final Identifier TELEGRAM = new Identifier("itemcooldown", "telegram.png");
    private final List<EntryListWidget<?>> listWidgets = new ArrayList<>();

    public MainScreen(Screen parent) {
        super(Text.of(""));
        this.parent = parent;
    }

    @Override
    protected void init() {
        children.clear();
        listWidgets.clear();
        buttons.clear();
        Map<ConfigData.Category, List<AbstractItemCooldown>> items = settings.items.getValue();
        ConfigData.Category category = settings.selectedCategory.getValue();
        itemsList = new ItemsListWidget(client, width, height, 96, height - 80, 30);
        if (items.get(category) != null) {
            items.get(category).forEach(item -> {
                ItemCustomElement element = new ItemCustomElement(item, this);
                element.init();
                itemsList.addEntry(new ItemsListWidget.Elements(element));
            });
        }
        CategoriesListWidget categoriesList = new CategoriesListWidget(client, 80, height, 12, 96, 30, this);
        categoriesList.setLeftPos(width / 2 - 40);
        categories.forEach(category1 -> categoriesList.addEntry(new CategoriesListWidget.Elements(category1)));
        CustomButtonWidget addItem = CustomButtonWidget.builder(Text.of("Добавить предмет"), press -> client.openScreen(new CreateItemCooldownScreen(this)))
                .dimensions(width / 2 - 60, height - 65, 120, 20)
                .build();
        CustomButtonWidget saveAndExit = CustomButtonWidget.builder(Text.of("Сохранить и выйти"), press -> onClose()).dimensions(width / 2 - 60, height - 35, 120, 20).build();
        CustomButtonWidget positionScreen = CustomButtonWidget.builder(Text.of("Настроить отображение"), press -> client.openScreen(new PositionScreen(this))).dimensions(10, height - 65, 140, 20).build();
        CustomButtonWidget modEnabled = CustomButtonWidget.builder(PositionScreen.getButtonMessage("Включить мод", settings.enabled), press -> {
            settings.enabled.setValue(!settings.enabled.getValue());
            press.setMessage(PositionScreen.getButtonMessage("Включить мод", settings.enabled));
        }).dimensions(20, height - 35, 120, 20).build();
        addChild(itemsList);
        addButton(addItem);
        addChild(categoriesList);
        addButton(saveAndExit);
        addButton(positionScreen);
        addButton(modEnabled);
        listWidgets.add(categoriesList);
        listWidgets.add(itemsList);
    }

    @Override
    public <T extends Element> T addChild(T child) {
        return super.addChild(child);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        RenderHelper.drawTexture(matrixStack, width - 37, height - 37, 27, 27, 0, 0, 128, 128, 128, 128, TELEGRAM);
        listWidgets.forEach(w -> w.render(matrixStack, mouseX, mouseY, delta));
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        settings.settingsList.forEach(Setting::save);
        client.openScreen(parent);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= width - 37 && mouseX <= width - 10 && mouseY >= height - 37 && mouseY <= height - 10) {
            client.openScreen(new ConfirmScreen(isConfirmed -> {
                if (isConfirmed) {
                    Util.getOperatingSystem().open("https://t.me/zyouime13");
                } else {
                    client.openScreen(this);
                }
            }, Text.of("§bЗмееныш13"),Text.of("Модики всякие")));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void clearAndInit() {
        double scroll = itemsList.getScrollAmount();
        children.clear();
        buttons.clear();
        init();
        itemsList.setScrollAmount(scroll);
    }
}
