package me.zyouime.itemcooldown.screen.widget;

import me.zyouime.itemcooldown.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Locale;

public class CustomSliderWidget extends SliderWidget implements Tooltip {

    private final int min, max;
    private final NumberSetting setting;
    private final String settingName;
    private List<Text> tooltip;

    public CustomSliderWidget(int x, int y, int width, int height, Text text, int min, int max, NumberSetting setting, String tooltip) {
        super(x, y, width, height, text, CustomSliderWidget.map(setting.getValue(), min, max, 0.0f, 1.0f));
        this.min = min;
        this.max = max;
        this.setting = setting;
        this.settingName = text.getString();
        if (tooltip != null) {
            this.tooltip = transformTooltip(tooltip);
        }
        this.updateMessage();
    }

    public CustomSliderWidget(int x, int y, int width, int height, Text text, int min, int max, NumberSetting setting) {
        this(x, y, width, height, text, min, max, setting, null);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);
        if (isHovered() && tooltip != null) {
            renderToolTip(matrices, mouseX, mouseY);
        }
    }

    @Override
    public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
        render(this, matrices, mouseX, mouseY);
    }

    public float toValue(double d) {
        return Float.parseFloat(String.format(Locale.US, "%.2f", CustomSliderWidget.map(d, 0.0, 1.0, this.min, this.max)));
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of(settingName + this.toValue(this.value)));
    }

    @Override
    protected void applyValue() {
        setting.setValue(this.toValue(this.value));
    }

    private static double map(double value, double inMin, double inMax, double outMin, double outMax) {
        return outMin + (value - inMin) * (outMax - outMin) / (inMax - inMin);
    }

    @Override
    public void render(ClickableWidget widget, MatrixStack matrixStack, int mouseX, int mouseY) {
        if (MinecraftClient.getInstance().currentScreen != null) {
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrixStack, tooltip, mouseX, mouseY);
        }
    }
}
