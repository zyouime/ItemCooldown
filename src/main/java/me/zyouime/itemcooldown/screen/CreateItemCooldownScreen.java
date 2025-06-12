package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import me.zyouime.itemcooldown.screen.widget.CustomButtonWidget;
import me.zyouime.itemcooldown.util.NbtHelper;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
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
            CustomButtonWidget exit = CustomButtonWidget.builder(Text.of("Выйти"), press -> onClose())
                    .dimensions(width / 2 - 40, height / 2, 80, 20)
                    .build();
            addButton(exit);
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
        CustomButtonWidget saveAndExitButton = CustomButtonWidget.builder(Text.of("Сохранить и выйти"), press -> {
                    ItemStack stackInHand = client.player.getStackInHand(Hand.MAIN_HAND);
                    if (stackInHand.isEmpty()) {
                        onClose();
                        return;
                    }
                    if (itemType == ItemTypes.CUSTOM && stackInHand.getTag() == null) {
                        onClose();
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
                        NbtCompound tag = stackInHand.getTag().copy();
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
                    onClose();
                }).dimensions(centerX - 160, centerY + 130, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        CustomButtonWidget cancelAndExit = CustomButtonWidget.builder(Text.of("Отменить и выйти"), press -> onClose()).dimensions(centerX + 10, centerY + 130, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        addButton(saveAndExitButton);
        addButton(cancelAndExit);
    }

    @Override
    protected void renderMsg(MatrixStack matrixStack) {
        String text = client.world == null ? "Чтобы добавить предмет, вы должны быть на сервере!" : "Держите в основной руке предмет, который хотите добавить!!!";
        int yOffset = client.world == null ? 50 : 130;
        RenderHelper.drawCenteredXYText(matrixStack, width / 2f, height / 2f - yOffset, 1.5f, text, Color.WHITE);
    }
}

