package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.util.NbtHelper;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.awt.*;

public class CreateItemCooldownScreen extends BaseItemCooldownScreen {

    public CreateItemCooldownScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        if (client.world == null) {
            ButtonWidget exit = ButtonWidget.builder(Text.literal("Выйти"), press -> close())
                    .dimensions(width / 2 - 40, height / 2, 80, 20)
                    .build();
            this.addDrawableChild(exit);
            return;
        }
        super.init();
    }

    @Override
    protected void initFields() {
        this.i = 0;
        this.itemType = ItemTypes.VANILLA;
        this.maxCooldown = 0;
    }

    @Override
    protected void createSaveAndExitWidgets() {
        ButtonWidget saveAndExitButton = ButtonWidget.builder(Text.literal("Сохранить и выйти"), press -> {
                    ItemStack stackInHand = client.player.getStackInHand(Hand.MAIN_HAND);
                    if (stackInHand.isEmpty()) {
                        close();
                        return;
                    }
                    if (itemType == ItemTypes.CUSTOM && stackInHand.getNbt() == null) {
                        close();
                        return;
                    }
                    AbstractItemCooldown newItem;
                    if (itemType == ItemTypes.VANILLA) {
                        newItem = VanillaItemCooldown.builder(stackInHand.getItem())
                                .setPos(0, 0)
                                .setMaxCooldown(maxCooldown)
                                .hasCustomCooldown(hasCustomCooldown)
                                .setWhenNoFightMode(setWhenNoFightMode)
                                .resetWhenNoFightMode(resetWhenNoFightMode)
                                .canUseWhenNoFightMode(canUseWhenNoFightMode)
                                .resetWhenLeftTheServer(resetWhenLeftTheServer)
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
                                .resetWhenLeftTheServer(resetWhenLeftTheServer)
                                .setNbt(tag.toString())
                                .build();
                    }
                    settings.items.getValue().get(settings.selectedCategory.getValue()).add(newItem);
                    close();
                }).dimensions(centerX - 160, centerY + 130, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        ButtonWidget cancelAndExit = ButtonWidget.builder(Text.literal("Отменить и выйти"), press -> close()).dimensions(centerX + 10, centerY + 130, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(saveAndExitButton);
        this.addDrawableChild(cancelAndExit);
    }

    @Override
    protected void renderMsg(DrawContext context) {
        String text = client.world == null ? "Чтобы добавить предмет, вы должны быть на сервере!" : "Держите в основной руке предмет, который хотите добавить!!!";
        int yOffset = client.world == null ? 50 : 130;
        RenderHelper.drawCenteredXYText(context, width / 2f, height / 2f - yOffset, 1.5f, text, Color.WHITE);
    }
}

