package me.zyouime.itemcooldown.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomButtonWidget extends ButtonWidget implements Tooltip {

    private final List<Text> tooltip;

    private CustomButtonWidget(Builder builder) {
        super(builder.x, builder.y, builder.width, builder.height, builder.msg, builder.onPress);
        this.tooltip = builder.tooltip;
    }

    @Override
    public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
        if (tooltip != null) {
            render(this, matrices, mouseX, mouseY);
        }
    }

    public static Builder builder(Text msg, PressAction onPress) {
        return new Builder(msg, onPress);
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void render(ClickableWidget widget, MatrixStack matrixStack, int mouseX, int mouseY) {
        if (MinecraftClient.getInstance().currentScreen != null) {
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrixStack, tooltip, mouseX, mouseY);
        }
    }

    public static class Builder {

        private final Text msg;
        private final PressAction onPress;
        private int x, y, width, height;
        private List<Text> tooltip = null;

        public Builder(Text msg, PressAction onPress) {
            this.msg = msg;
            this.onPress = onPress;
        }

        public Builder dimensions(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }
        public Builder tooltip(String tooltip) {
            this.tooltip = new ArrayList<>();
            for (String lines : tooltip.split("\n")) {
                this.tooltip.add(Text.of(lines));
            }
            return this;
        }

        public CustomButtonWidget build() {
            return new CustomButtonWidget(this);
        }
    }
}
