package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.config.ModConfig;
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
    private int cooldown;

    protected AbstractItemCooldown(ItemStack item, int maxCooldown, float x, float y, boolean resetWhenNoFightMode, boolean setWhenNoFightMode, boolean canUseWhenNoFightMode) {
        this.item = item;
        this.maxCooldown = maxCooldown * 20;
        this.x = x;
        this.y = y;
        this.resetWhenNoFightMode = resetWhenNoFightMode;
        this.setWhenNoFightMode = setWhenNoFightMode;
        this.canUseWhenNoFightMode = canUseWhenNoFightMode;
    }

    public void render(DrawContext context) {
        MatrixStack matrixStack = context.getMatrices();
        ConfigData configData = ModConfig.configData;
        float scale = (float) configData.getField("scale");
        float sWidth = context.getScaledWindowWidth();
        float sHeight = context.getScaledWindowHeight();
        float centerX = sWidth / 2f;
        float centerY = sHeight / 2f;
        float maxX = (sWidth / scale) - 20;
        float maxY = (sHeight / scale) - 24;
        float renderX = Math.max(0, Math.min(centerX / scale + x, maxX));
        float renderY = Math.max(0, Math.min(centerY / scale + y, maxY));
        matrixStack.push();
        matrixStack.scale(scale, scale, 1.0f);
        RenderHelper.drawRoundedRect(matrixStack, renderX, renderY, 20, 24, 3, this.getBackgroundColor());
        RenderHelper.drawItem(context, this.getItem(), renderX + 2, renderY + 1);
        String text = (cooldown / 20) + " сек";
        float textScale = scale / 4.44f;
        float textX = renderX + (20 - RenderHelper.textRenderer.getWidth(text) * textScale) / 2;
        RenderHelper.drawCenteredYText(context, textX, renderY + 16, textScale, text, Color.YELLOW);
        matrixStack.pop();
    }


    public void tick() {
        --cooldown;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public ItemStack setCount(int count) {
        this.item.setCount(count);
        return this.item;
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

    public void updatePos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void shouldSetCooldown(ItemStack usedItem) {
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    private Color getBackgroundColor() {
        if (canUseWhenNoFightMode && !EventManager.isPvP()) {
            return new Color(0, 255, 0, 96);
        }
        return new Color(0, 0, 0, 96);
    }

}
