package me.zyouime.itemcooldown.mixin;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Unique
    private float scale, scaledCenterX, scaledCenterY, scaledWidth, scaledHeight, x, y, offsetX, offsetY;
    @Unique
    private boolean isDragging;
    @Unique
    private Window window;
    @Unique
    private AbstractItemCooldown draggingItem;
    @Unique
    private Map<ConfigData.Category, List<AbstractItemCooldown>> items;
    @Unique
    private ConfigData.Category category;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        ItemCooldown itemCooldown = ItemCooldown.getInstance();
        items = itemCooldown.settings.items.getValue();
        scale = itemCooldown.settings.scale.getValue();
        category = itemCooldown.settings.selectedCategory.getValue();
        window = this.client.getWindow();
        scaledWidth = window.getScaledWidth();
        scaledHeight = window.getScaledHeight();
        scaledCenterX = scaledWidth / 2f;
        scaledCenterY = scaledHeight / 2f;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
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
            x = ((float) mouseX - scaledCenterX - offsetX) / scale;
            y = ((float) mouseY - scaledCenterY - offsetY) / scale;
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
        isDragging = false;
        saveToConfig();
        draggingItem = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Unique
    private void saveToConfig() {
        if (draggingItem != null) {
            ItemCooldown.getInstance().settings.items.updatePos(category, draggingItem, x, y);
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
