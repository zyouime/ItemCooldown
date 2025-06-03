package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

public class PositionScreen extends Screen {

    private final Screen parent;
    private float scale;
    private float scaledCenterX;
    private float scaledCenterY;
    private float scaledWidth;
    private float scaledHeight;
    private float offsetX;
    private float offsetY;
    private boolean isDragging;
    private AbstractItemCooldown draggingItem;
    private Map<ConfigData.Category, List<AbstractItemCooldown>> items;
    private ConfigData.Category category;

    public PositionScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    public void init() {
        ItemCooldown itemCooldown = ItemCooldown.getInstance();
        items = itemCooldown.settings.items.getValue();
        scale = itemCooldown.settings.scale.getValue();
        category = itemCooldown.settings.selectedCategory.getValue();
        Window window = this.client.getWindow();
        scaledWidth = window.getScaledWidth();
        scaledHeight = window.getScaledHeight();
        scaledCenterX = scaledWidth / 2f;
        scaledCenterY = scaledHeight / 2f;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        if (items.get(category) == null) return;
        items.get(category).forEach(aic -> aic.render(context));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (AbstractItemCooldown item : items.get(category)) {
            float itemX = Math.max(0, Math.min(scaledCenterX / scale + item.getX(), (scaledWidth / scale) - 20));
            float itemY = Math.max(0, Math.min(scaledCenterY / scale + item.getY(), (scaledHeight / scale) - 24));
            if (mouseX >= itemX * scale && mouseX <= (itemX + 20) * scale && mouseY >= itemY * scale && mouseY <= (itemY + 24) * scale) {
                isDragging = true;
                offsetX = (float) mouseX - (itemX * scale);
                offsetY = (float) mouseY - (itemY * scale);
                draggingItem = item;
                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && draggingItem != null) {
            float x = ((float) mouseX - scaledCenterX - offsetX) / scale;
            float y = ((float) mouseY - scaledCenterY - offsetY) / scale;
            float minX = -scaledCenterX / scale;
            float minY = -scaledCenterY / scale;
            float maxX = (scaledCenterX / scale) - 20;
            float maxY = (scaledCenterY / scale) - 24;
            x = Math.max(minX, Math.min(x, maxX));
            y = Math.max(minY, Math.min(y, maxY));
            draggingItem.setX(x);
            draggingItem.setY(y);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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
        ItemCooldown.getInstance().settings.settingsList.forEach(Setting::save);
        this.client.setScreen(parent);
    }
}
