package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.screen.widget.CustomSliderWidget;
import me.zyouime.itemcooldown.setting.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;
import static me.zyouime.itemcooldown.item.AbstractItemCooldown.*;

public class PositionScreen extends Screen {

    private final Screen parent;
    private float scale;
    private float centerX;
    private float centerY;
    private float offsetX;
    private float offsetY;
    private boolean isDragging;
    private AbstractItemCooldown draggingItem;
    private Map<ConfigData.Category, List<AbstractItemCooldown>> items;
    private ConfigData.Category category;
    private final ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;

    public PositionScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    public void init() {
        items = settings.items.getValue();
        category = settings.selectedCategory.getValue();
        centerX = width / 2f;
        centerY = height / 2f;
        CustomSliderWidget sliderWidget = new CustomSliderWidget((int) centerX - 115, height - 90, 120, 20, Text.of("Размеры иконок: "), 1, 3, settings.scale);
        CustomSliderWidget indentSlider = new CustomSliderWidget((int) (centerX + 15), height - 90, 80, 20, Text.literal("Отступы: "), 0, 5, settings.indent);
        indentSlider.setTooltip(Tooltip.of(Text.literal("Расстояние в пикселях, на котором будут находиться иконки при \"прилипании\"")));
        ButtonWidget alignment = ButtonWidget.builder(getButtonMessage("Выравнивание", settings.alignment), press -> {
            settings.alignment.setValue(!settings.alignment.getValue());
            indentSlider.active = settings.alignment.getValue();
            press.setMessage(getButtonMessage("Выравнивание", settings.alignment));
        }).dimensions((int) centerX - 100, height - 60, 120, 20).build();
        alignment.setTooltip(Tooltip.of(Text.literal("Включите, если хотите, чтобы иконки \"прилипали\" друг к другу на выбранном отступе")));
        ButtonWidget save = ButtonWidget.builder(Text.literal("Сохранить"), press -> this.close()).dimensions((int) centerX - 40, height - 30, 80, 20).build();
        ButtonWidget renderBackground = ButtonWidget.builder(getButtonMessage("Фон", settings.renderBackground), press -> {
            settings.renderBackground.setValue(!settings.renderBackground.getValue());
            press.setMessage(getButtonMessage("Фон", settings.renderBackground));
        }).dimensions((int) centerX + 30, height - 60, 60, 20).build();
        renderBackground.setTooltip(Tooltip.of(Text.literal("Задний фон иконок")));
        this.addDrawableChild(indentSlider);
        this.addDrawableChild(alignment);
        this.addDrawableChild(save);
        this.addDrawableChild(renderBackground);
        this.addDrawableChild(sliderWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        if (items.get(category) == null) return;
        scale = settings.scale.getValue();
        items.get(category).forEach(aic -> aic.render(context));
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (items.get(category) != null) {
            for (AbstractItemCooldown item : items.get(category)) {
                float itemX = Math.max(0, Math.min(centerX / scale + item.getX(), (width / scale) - ICON_WIDTH));
                float itemY = Math.max(0, Math.min(centerY / scale + item.getY(), (height / scale) - ICON_HEIGHT));
                if (mouseX >= itemX * scale && mouseX <= (itemX + ICON_WIDTH) * scale && mouseY >= itemY * scale && mouseY <= (itemY + ICON_HEIGHT) * scale) {
                    isDragging = true;
                    offsetX = (float) mouseX - (itemX * scale);
                    offsetY = (float) mouseY - (itemY * scale);
                    draggingItem = item;
                    break;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && draggingItem != null && items.get(category) != null) {
            float x = ((float) mouseX - centerX - offsetX) / scale;
            float y = ((float) mouseY - centerY - offsetY) / scale;
            float minX = -centerX / scale;
            float minY = -centerY / scale;
            float maxX = (centerX / scale) - ICON_WIDTH;
            float maxY = (centerY / scale) - ICON_HEIGHT;
            x = Math.max(minX, Math.min(x, maxX));
            y = Math.max(minY, Math.min(y, maxY));
            float indent = settings.indent.getValue();
            if (settings.alignment.getValue()) {
                for (AbstractItemCooldown item : items.get(category)) {
                    if (item == draggingItem) continue;
                    float itemX = item.getX();
                    float itemY = item.getY();
                    boolean isVertical = (y + ICON_HEIGHT > itemY) && (y < itemY + ICON_HEIGHT);
                    if (isVertical) {
                        float right = itemX + ICON_WIDTH + indent;
                        if (Math.abs(x - right) <= indent) {
                            x = right;
                            y = itemY;
                        }
                        float left = itemX - ICON_WIDTH - indent;
                        if (Math.abs((x + ICON_WIDTH) - itemX) <= indent) {
                            x = left;
                            y = itemY;
                        }
                    }
                    boolean isHorizontal = (x + ICON_WIDTH > itemX) && (x < itemX + ICON_WIDTH);
                    if (isHorizontal) {
                        float bottom = itemY + ICON_HEIGHT + indent;
                        if (Math.abs(y - bottom) <= indent) {
                            y = bottom;
                            x = itemX;
                        }
                        float top = itemY - ICON_HEIGHT - indent;
                        if (Math.abs((y + ICON_HEIGHT) - itemY) <= indent) {
                            y = top;
                            x = itemX;
                        }
                    }
                }
            }
            draggingItem.setX(x);
            draggingItem.setY(y);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public static Text getButtonMessage(String option, BooleanSetting setting) {
        return Text.literal(option + ": " + (setting.getValue() ? "§atrue" : "§cfalse"));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDragging && draggingItem != null) {
            isDragging = false;
            draggingItem = null;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
