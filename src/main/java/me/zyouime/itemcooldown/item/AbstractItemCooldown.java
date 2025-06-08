package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class AbstractItemCooldown {

    @Expose
    private ItemStack item;
    @Expose
    private int maxCooldown;
    @Expose
    private float x, y;
    @Expose
    protected boolean resetWhenNoFightMode;
    @Expose
    protected boolean setWhenNoFightMode;
    @Expose
    protected boolean canUseWhenNoFightMode;
    @Expose
    private boolean visible = true;
    @Expose
    private boolean resetWhenLeftTheServer;
    private int cooldown;
    public static final float ICON_WIDTH = 20f;
    public static final float ICON_HEIGHT = 24f;

    protected AbstractItemCooldown(ItemStack item, int maxCooldown, float x, float y, boolean resetWhenNoFightMode, boolean setWhenNoFightMode, boolean canUseWhenNoFightMode, boolean resetWhenLeftTheServer) {
        this.item = item;
        this.maxCooldown = maxCooldown * 20;
        this.x = x;
        this.y = y;
        this.resetWhenNoFightMode = resetWhenNoFightMode;
        this.setWhenNoFightMode = setWhenNoFightMode;
        this.canUseWhenNoFightMode = canUseWhenNoFightMode;
        this.resetWhenLeftTheServer = resetWhenLeftTheServer;
    }

    public void render(DrawContext context) {
        if (!visible) return;
        MatrixStack matrixStack = context.getMatrices();
        float scale = ItemCooldown.getInstance().settings.scale.getValue();
        float sWidth = context.getScaledWindowWidth();
        float sHeight = context.getScaledWindowHeight();
        float centerX = sWidth / 2f;
        float centerY = sHeight / 2f;
        float maxX = (sWidth / scale) - ICON_WIDTH;
        float maxY = (sHeight / scale) - ICON_HEIGHT;
        float renderX = Math.max(0, Math.min(centerX / scale + x, maxX));
        float renderY = Math.max(0, Math.min(centerY / scale + y, maxY));
        matrixStack.push();
        matrixStack.scale(scale, scale, 1.0f);
        if (ItemCooldown.getInstance().settings.renderBackground.getValue()) {
            RenderHelper.drawRoundedRect(matrixStack, renderX, renderY, ICON_WIDTH, ICON_HEIGHT, 3, getBackgroundColor());
        }
        RenderHelper.drawItem(context, this.getItem(), renderX + 2, renderY + 1);
        matrixStack.pop();
        int seconds = cooldown / 20;
        String text = String.valueOf(seconds);
        float textX = renderX * scale + (20 * scale - RenderHelper.textRenderer.getWidth(text) * (scale / 2f)) / 2f;
        RenderHelper.drawCenteredYText(context, textX, (renderY + 17) * scale, scale / 2f, text, getTextColor());
    }

    public void tick() {
        --cooldown;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setCount(int count) {
        this.item.setCount(count);
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public float getX() {
        return x;
    }

    public boolean isResetWhenNoFightMode() {
        return resetWhenNoFightMode;
    }

    public boolean isSetWhenNoFightMode() {
        return setWhenNoFightMode;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isCanUseWhenNoFightMode() {
        return canUseWhenNoFightMode;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public boolean shouldSetCooldown(ItemStack usedItem) {
        return false;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean isResetWhenLeftTheServer() {
        return resetWhenLeftTheServer;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    private Color getTextColor() {
        return !ItemCooldown.getInstance().settings.renderBackground.getValue() && canUseWhenNoFightMode && !EventManager.isPvP() ? Color.GREEN : Color.YELLOW;
    }

    private Color getBackgroundColor() {
        if (canUseWhenNoFightMode && !EventManager.isPvP()) {
            return new Color(0, 255, 0, 96);
        }
        return new Color(0, 0, 0, 96);
    }

}
