package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.util.NbtHelper;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

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
        if (item instanceof VanillaItemCooldown vanillaItemCooldown) {
            this.hasCustomCooldown = vanillaItemCooldown.isHasCustomCooldown();
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
        ButtonWidget saveAndExitButton = ButtonWidget.builder(Text.literal("Сохранить и выйти"), press -> {
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
                    close();
                }).dimensions(centerX - 160, centerY + 130, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        ButtonWidget cancelAndExit = ButtonWidget.builder(Text.literal("Отменить и выйти"), press -> close()).dimensions(centerX + 10, centerY + 130, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(saveAndExitButton);
        this.addDrawableChild(cancelAndExit);
    }

    @Override
    protected void renderMsg(DrawContext context) {
        RenderHelper.drawCenteredXYText(context, centerX, centerY - 130, 1.5f, "Редактирование предмета из категории " + settings.selectedCategory.getValue().name, Color.WHITE);
    }
}

