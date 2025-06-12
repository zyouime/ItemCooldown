package me.zyouime.itemcooldown.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class CustomTextFieldWidget extends TextFieldWidget implements Tooltip {

    private final List<Text> tooltip;

    public CustomTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, String tooltip) {
        super(textRenderer, x, y, width, height, Text.of(""));
        this.tooltip = transformTooltip(tooltip);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);
        if (isHovered()) {
            renderToolTip(matrices, mouseX, mouseY);
        }
    }

    @Override
    public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
        render(this, matrices, mouseX, mouseY);
    }

    @Override
    public void render(ClickableWidget widget, MatrixStack matrixStack, int mouseX, int mouseY) {
        if (MinecraftClient.getInstance().currentScreen != null) {
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrixStack, tooltip, mouseX, mouseY);
        }
    }
}
