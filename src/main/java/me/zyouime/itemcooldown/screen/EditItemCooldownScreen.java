package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.screen.widget.CustomButtonWidget;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.List;

public class EditItemCooldownScreen extends BaseItemCooldownScreen {

    private final AbstractItemCooldown item;

    public EditItemCooldownScreen(Screen parent, AbstractItemCooldown item) {
        super(parent);
        this.item = item;
    }

    @Override
    protected void init() {
        super.init();
        this.itemTypeButton.active = false;
    }

    @Override
    protected void initFields() {
        if (item instanceof VanillaItemCooldown) {
            this.hasCustomCooldown = ((VanillaItemCooldown) item).isHasCustomCooldown();
            this.itemType = ItemTypes.VANILLA;
        } else {
            this.itemType = ItemTypes.CUSTOM;
        }
        this.maxCooldown = item.getMaxCooldown() / 20;
        this.setWhenNoFightMode = item.isSetWhenNoFightMode();
        this.resetWhenNoFightMode = item.isResetWhenNoFightMode();
        this.canUseWhenNoFightMode = item.isCanUseWhenNoFightMode();
        this.resetWhenLeftTheServer = item.isResetWhenLeftTheServer();
        for (int index = 0; index < itemTypes.length; index++) {
            if (itemTypes[index] == itemType) {
                this.i = index;
                break;
            }
        }
    }

    @Override
    protected void createSaveAndExitWidgets() {
        CustomButtonWidget saveAndExitButton = CustomButtonWidget.builder(Text.of("Сохранить и выйти"), press -> {
                    AbstractItemCooldown modifiedItem;
                    if (itemType == ItemTypes.VANILLA) {
                        modifiedItem = VanillaItemCooldown.builder(item.getItem().getItem())
                                .setPos(item.getX(), item.getY())
                                .setMaxCooldown(maxCooldown)
                                .hasCustomCooldown(hasCustomCooldown)
                                .setWhenNoFightMode(setWhenNoFightMode)
                                .resetWhenNoFightMode(resetWhenNoFightMode)
                                .canUseWhenNoFightMode(canUseWhenNoFightMode)
                                .resetWhenLeftTheServer(resetWhenLeftTheServer)
                                .build();
                    } else {
                        modifiedItem = CustomItemCooldown.builder(item.getItem().getItem())
                                .setPos(item.getX(), item.getY())
                                .setMaxCooldown(maxCooldown)
                                .setWhenNoFightMode(setWhenNoFightMode)
                                .canUseWhenNoFightMode(canUseWhenNoFightMode)
                                .resetWhenNoFightMode(resetWhenNoFightMode)
                                .setNbt(((CustomItemCooldown) item).nbt.toString())
                                .resetWhenLeftTheServer(resetWhenLeftTheServer)
                                .build();
                    }
                    List<AbstractItemCooldown> list = settings.items.getValue().get(settings.selectedCategory.getValue());
                    list.set(list.indexOf(item), modifiedItem);
                    onClose();
                }).dimensions(centerX - 160, centerY + 130, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        CustomButtonWidget cancelAndExit = CustomButtonWidget.builder(Text.of("Отменить и выйти"), press -> onClose()).dimensions(centerX + 10, centerY + 130, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        addButton(saveAndExitButton);
        addButton(cancelAndExit);
    }

    @Override
    protected void renderMsg(MatrixStack matrixStack) {
        RenderHelper.drawCenteredXYText(matrixStack, centerX, centerY - 130, 1.5f, "Редактирование предмета из категории " + settings.selectedCategory.getValue().name, Color.WHITE);
    }
}

