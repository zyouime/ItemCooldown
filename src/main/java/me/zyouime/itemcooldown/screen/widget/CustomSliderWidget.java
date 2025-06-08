package me.zyouime.itemcooldown.screen.widget;

import me.zyouime.itemcooldown.setting.NumberSetting;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Locale;

public class CustomSliderWidget extends SliderWidget {

    private final int min, max;
    private final NumberSetting setting;
    private final String settingName;

    public CustomSliderWidget(int x, int y, int width, int height, Text text, int min, int max, NumberSetting setting) {
        super(x, y, width, height, text, MathHelper.map(setting.getValue(), min, max, 0.0f, 1.0f));
        this.min = min;
        this.max = max;
        this.setting = setting;
        this.settingName = text.getString();
        this.updateMessage();
    }
    public float toValue(double d) {
        return Float.parseFloat(String.format(Locale.US, "%.2f", MathHelper.map(d, 0.0, 1.0, this.min, this.max)));
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of(settingName + this.toValue(this.value)));
    }

    @Override
    protected void applyValue() {
        setting.setValue(this.toValue(this.value));
    }
}
