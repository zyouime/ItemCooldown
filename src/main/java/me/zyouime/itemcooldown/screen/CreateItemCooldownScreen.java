package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.util.NbtHelper;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.awt.*;
import java.util.function.Consumer;

public class CreateItemCooldownScreen extends Screen {

    private final Screen parent;
    private final ItemTypes[] itemTypes = new ItemTypes[]{ItemTypes.VANILLA, ItemTypes.CUSTOM};
    private int i = 0;
    private int centerX, centerY;
    private final ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;
    private ItemTypes itemType = ItemTypes.VANILLA;
    private boolean resetWhenNoFightMode;
    private boolean setWhenNoFightMode;
    private boolean canUseWhenNoFightMode;
    private int maxCooldown;
    private boolean hasCustomCooldown;
    private ButtonWidget itemTypeButton, resetWhenNoFightModeButton, setWhenNoFightModeButton, canUseWhenNoFightModeButton, hasCustomCooldownButton;
    private TextFieldWidget maxCooldownField;
    private final int BUTTON_WIDTH = 160;
    private final int BUTTON_HEIGHT = 20;

    public CreateItemCooldownScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
        if (client.world == null) {
            ButtonWidget exit = ButtonWidget.builder(Text.literal("Выйти"), press -> close())
                    .dimensions(centerX - 40, centerY, 80, 20).build();
            this.addDrawableChild(exit);
            return;
        }
        itemTypeButton = ButtonWidget.builder(setItemType(), press -> {
            i++;
            if (i >= itemTypes.length) {
                i = 0;
            }
            itemType = itemTypes[i];
            setWidgetsActive();
            press.setMessage(setItemType());
        }).dimensions(centerX - 80, centerY - 80, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        itemTypeButton.setTooltip(Tooltip.of(Text.literal("Выберите тип предмета"
                + "\nVanilla - это те, которые имеют ванильный кулдаун (серенькое такое поверх предмета, например трапка на холике/фт)"
                + "\nCustom - это те, которые имеют кастомный кулдаун (например как исцеление на холике) и визуально не показывается")));
        resetWhenNoFightModeButton = createOptionButton(-50, "ResetWhenNoFightMode", resetWhenNoFightMode, newValue -> resetWhenNoFightMode = newValue, Text.literal("Выберите, если после режима боя кд сбрасывается"
                                + "\nПример: вы использовали предмет за 5 сек до конца режима боя, на него навесилось кд 20 сек,"
                                + "\nно после выхода из режима боя кд сбросился, и войдя снова в него, вы можете использовать предмет"));
        setWhenNoFightModeButton = createOptionButton(-20, "SetWhenNoFightMode", setWhenNoFightMode, newValue -> setWhenNoFightMode = newValue, Text.literal("Выберите, если кд на предмет устанавливается при каждом использовании, даже если вы не в пвп"));
        canUseWhenNoFightModeButton = createOptionButton(+10, "CanUseWhenNoFightMode", canUseWhenNoFightMode, newValue -> canUseWhenNoFightMode = newValue, Text.literal("Выберите, если после выхода из режима боя вы можете использовать предмет,"
                        + " но кд при этом не убирается, и войдя в режия боя снова, будет сразу же кд"));
        hasCustomCooldownButton = createOptionButton(+70, "HasCustomCooldown", hasCustomCooldown, newValue -> {
            hasCustomCooldown = newValue;
            setWidgetsActive();
            }, Text.literal("Выберите, если на ванильном предмете есть кастомный кулдаун"
                                + "\nПример: на холике если хорус использовать в режиме боя, то на него навешивается 15 сек дополнительного кд, "
                                + "\nкоторое визуально не отображается"));
        maxCooldownField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, centerX - 80, centerY + 40, BUTTON_WIDTH, BUTTON_HEIGHT, Text.empty());
        maxCooldownField.setPlaceholder(Text.literal("§70"));
        maxCooldownField.setChangedListener(this::changedListener);
        maxCooldownField.setTooltip(Tooltip.of(Text.literal("Кулдаун, который навешивается при использовании предмета"
                + "\n(Только для кастомных предметов или ванильных с кастомным кулдауном)")));
        ButtonWidget saveAndExitButton = ButtonWidget.builder(Text.literal("Сохранить и выйти"), press -> {
            ItemStack stackInHand = client.player.getStackInHand(Hand.MAIN_HAND);
            if (stackInHand.isEmpty()) close();
            if (stackInHand.getNbt() == null && itemType.equals(ItemTypes.CUSTOM)) close();
            AbstractItemCooldown newItem;
            if (itemType.equals(ItemTypes.VANILLA)) {
                newItem = VanillaItemCooldown.builder(stackInHand.getItem())
                        .setPos(0, 0)
                        .setMaxCooldown(maxCooldown)
                        .setWhenNoFightMode(setWhenNoFightMode)
                        .resetWhenNoFightMode(resetWhenNoFightMode)
                        .canUseWhenNoFightMode(canUseWhenNoFightMode)
                        .build();
            } else {
                NbtCompound tag = stackInHand.getNbt().copy();
                NbtHelper.removeExtraKeys(tag);
                newItem = CustomItemCooldown.builder(stackInHand.getItem())
                        .setPos(0, 0)
                        .setMaxCooldown(maxCooldown)
                        .setWhenNoFightMode(setWhenNoFightMode)
                        .canUseWhenNoFightMode(canUseWhenNoFightMode)
                        .resetWhenNoFightMode(resetWhenNoFightMode)
                        .setNbt(tag.toString())
                        .build();
            }
            settings.items.getValue().get(settings.selectedCategory.getValue()).add(newItem);
            close();
        }).dimensions(centerX - 160, centerY + 100, 160, 20).build();
        ButtonWidget cancelAndExit = ButtonWidget.builder(Text.literal("Отменить и выйти"), press -> close()).dimensions(centerX + 10, centerY + 100, 160, 20).build();
        setWidgetsActive();
        this.addDrawableChild(saveAndExitButton);
        this.addDrawableChild(cancelAndExit);
        this.addDrawableChild(resetWhenNoFightModeButton);
        this.addDrawableChild(setWhenNoFightModeButton);
        this.addDrawableChild(hasCustomCooldownButton);
        this.addDrawableChild(maxCooldownField);
        this.addDrawableChild(canUseWhenNoFightModeButton);
        this.addDrawableChild(itemTypeButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        if (client.world == null) {
            RenderHelper.drawCenteredXYText(context, centerX, centerY - 30, 1.5f, "Чтобы добавить новый предмет, ты должен быть на сервере", Color.WHITE);
        } else RenderHelper.drawCenteredXYText(context, centerX, centerY - 130, 1.5f, "Держите в основной руке предмет, который хотите добавить!!!", Color.WHITE);
        super.render(context, mouseX, mouseY, delta);
    }

    private Text setItemType() {
        return Text.literal("ItemType: " + itemTypes[i]);
    }

    private Text getButtonMessage(String option, boolean value) {
        return Text.literal(option + ": " + (value ? "§atrue" : "§cfalse"));
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    private ButtonWidget createOptionButton(int yOffset, String optionName, boolean value, Consumer<Boolean> consumer, Text tooltip) {
        ButtonWidget button = ButtonWidget.builder(getButtonMessage(optionName, value), press -> {
            boolean newValue = !value;
            consumer.accept(newValue);
            press.setMessage(getButtonMessage(optionName, newValue));
        }).dimensions(centerX - BUTTON_WIDTH / 2, centerY + yOffset, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        button.setTooltip(Tooltip.of(tooltip));
        return button;
    }

    private void setWidgetsActive() {
        hasCustomCooldownButton.active = itemType.equals(itemTypes[0]);
        maxCooldownField.active = (itemType.equals(itemTypes[1]) || (itemType.equals(itemTypes[0])) && hasCustomCooldown);
        if (!maxCooldownField.active) {
            maxCooldownField.setEditableColor(Color.GRAY.getRGB());
        } else changedListener(maxCooldownField.getText());
    }

    private void changedListener(String string) {
        try {
            maxCooldown = Integer.parseInt(string);
            maxCooldownField.setEditableColor(Color.WHITE.getRGB());
        } catch (NumberFormatException ignore) {
            maxCooldownField.setEditableColor(Color.RED.getRGB());
        }
    }

    private enum ItemTypes {
        VANILLA,
        CUSTOM
    }
}
