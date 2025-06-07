package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseItemCooldownScreen extends Screen {

    protected final Screen parent;
    protected final ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;
    protected int centerX, centerY;
    protected final ItemTypes[] itemTypes = new ItemTypes[]{ItemTypes.VANILLA, ItemTypes.CUSTOM};
    protected int i;
    protected ItemTypes itemType;
    protected boolean resetWhenNoFightMode;
    protected boolean setWhenNoFightMode;
    protected boolean canUseWhenNoFightMode;
    protected boolean hasCustomCooldown;
    protected boolean resetWhenLeftTheServer = true;
    protected int maxCooldown;
    protected ButtonWidget itemTypeButton;
    protected ButtonWidget resetWhenNoFightModeButton;
    protected ButtonWidget setWhenNoFightModeButton;
    protected ButtonWidget canUseWhenNoFightModeButton;
    protected ButtonWidget hasCustomCooldownButton;
    protected ButtonWidget resetWhenLeftTheServerButton;
    protected TextFieldWidget maxCooldownField;
    protected final int BUTTON_WIDTH = 160;
    protected final int BUTTON_HEIGHT = 20;

    public BaseItemCooldownScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
        initFields();
        createOptionWidgets();
        createSaveAndExitWidgets();
        setWidgetsActive();
    }

    @Override
    public final void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        renderMsg(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    protected abstract void initFields();

    protected abstract void createSaveAndExitWidgets();

    protected abstract void renderMsg(DrawContext context);

    private void createOptionWidgets() {
        itemTypeButton = ButtonWidget.builder(setItemType(), press -> {
                    i++;
                    if (i >= itemTypes.length) {
                        i = 0;
                    }
                    itemType = itemTypes[i];
                    setWidgetsActive();
                    press.setMessage(setItemType());
                }).dimensions(centerX - BUTTON_WIDTH / 2, centerY - 80, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        itemTypeButton.setTooltip(Tooltip.of(Text.literal(
                "Выберите тип предмета"
                        + "\nVanilla - это те, которые имеют ванильный кулдаун (серенькое такое поверх предмета, например трапка на холике/фт)"
                        + "\nCustom - это те, которые имеют кастомный кулдаун (например как исцеление на холике) и визуально не показывается")));
        resetWhenNoFightModeButton = createOptionButton(-50, "ResetWhenNoFightMode", () -> this.resetWhenNoFightMode, newValue -> resetWhenNoFightMode = newValue, Text.literal("Выберите, если после режима боя кд сбрасывается"
                        + "\nПример: вы использовали предмет за 5 сек до конца режима боя, на него навесилось кд 20 сек,"
                        + "\nно после выхода из режима боя кд сбросился, и войдя снова в него, вы можете использовать предмет"));
        setWhenNoFightModeButton = createOptionButton(-20, "SetWhenNoFightMode", () -> this.setWhenNoFightMode, newValue -> setWhenNoFightMode = newValue, Text.literal("Выберите, если кд на предмет устанавливается при каждом использовании, даже если вы не в пвп"
                + "\n(Для ванильных предметов этот параметр влияет только если он имеет HasCustomCooldown)"));
        canUseWhenNoFightModeButton = createOptionButton(10, "CanUseWhenNoFightMode", () -> this.canUseWhenNoFightMode, newValue -> canUseWhenNoFightMode = newValue, Text.literal("Выберите, если после выхода из режима боя вы можете использовать предмет,"
                        + " но кд при этом не убирается, и войдя в бой снова, будет сразу же кд"));
        hasCustomCooldownButton = createOptionButton(70, "HasCustomCooldown", () -> this.hasCustomCooldown, newValue -> {
            hasCustomCooldown = newValue;
            setWidgetsActive();
            }, Text.literal("Выберите, если на ванильном предмете есть кастомный кулдаун"
                        + "\nПример: на холике если хорус использовать в режиме боя, то на него навешивается 15 сек дополнительного кд,"
                        + "\nкоторое визуально не отображается"));
        resetWhenLeftTheServerButton = createOptionButton(100, "ResetWhenLeftTheServer", () -> this.resetWhenLeftTheServer, newValue -> resetWhenLeftTheServer = newValue, Text.literal("Выберите, если после выхода из сервера (анки) кд на предмет сбрасывается"));
        maxCooldownField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, centerX - BUTTON_WIDTH / 2, centerY + 40, BUTTON_WIDTH, BUTTON_HEIGHT, Text.empty());
        maxCooldownField.setChangedListener(this::changedListener);
        maxCooldownField.setPlaceholder(Text.literal("§7" + maxCooldown));
        maxCooldownField.setTooltip(Tooltip.of(Text.literal("Кулдаун, который навешивается при использовании предмета в секундах"
                + "\n(Только для кастомных предметов или ванильных с кастомным кулдауном)")));
        this.addDrawableChild(resetWhenNoFightModeButton);
        this.addDrawableChild(setWhenNoFightModeButton);
        this.addDrawableChild(hasCustomCooldownButton);
        this.addDrawableChild(maxCooldownField);
        this.addDrawableChild(canUseWhenNoFightModeButton);
        this.addDrawableChild(itemTypeButton);
        this.addDrawableChild(resetWhenLeftTheServerButton);
    }

    private Text setItemType() {
        return Text.literal("ItemType: " + itemTypes[i]);
    }

    protected ButtonWidget createOptionButton(int yOffset, String optionName, Supplier<Boolean> value, Consumer<Boolean> consumer, Text tooltip) {
        ButtonWidget button = ButtonWidget.builder(getButtonMessage(optionName, value.get()), press -> {
            boolean newValue = !value.get();
            consumer.accept(newValue);
            press.setMessage(getButtonMessage(optionName, newValue));
        }).dimensions(centerX - BUTTON_WIDTH / 2, centerY + yOffset, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        button.setTooltip(Tooltip.of(tooltip));
        return button;
    }

    private Text getButtonMessage(String option, boolean value) {
        return Text.literal(option + ": " + (value ? "§atrue" : "§cfalse"));
    }

    private void setWidgetsActive() {
        hasCustomCooldownButton.active = itemType.equals(itemTypes[0]);
        maxCooldownField.active = (itemType.equals(itemTypes[1]) || (itemType.equals(itemTypes[0])) && hasCustomCooldown);
        if (!maxCooldownField.active) {
            maxCooldownField.setEditableColor(Color.GRAY.getRGB());
        } else changedListener(maxCooldownField.getText());
    }

    protected void changedListener(String string) {
        try {
            maxCooldown = Integer.parseInt(string);
            maxCooldownField.setEditableColor(Color.WHITE.getRGB());
        } catch (NumberFormatException ignore) {
            maxCooldownField.setEditableColor(Color.RED.getRGB());
        }
    }

    protected enum ItemTypes {
        VANILLA,
        CUSTOM
    }
}

