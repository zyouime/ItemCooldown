package me.zyouime.itemcooldown.item;

import com.google.gson.annotations.Expose;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.event.EventManager;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Objects;

public class AbstractItemCooldown {

    @Expose
    private String item;
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
        this.item = Registries.ITEM.getId(item.getItem()).toString();
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
        float scaledCenterX = context.getScaledWindowWidth() / 2f / scale;
        float scaledCenterY = context.getScaledWindowHeight() / 2f / scale;
        matrixStack.push();
        matrixStack.scale(scale, scale, 1.0f);
        RenderHelper.drawRoundedRect(matrixStack, scaledCenterX + x, scaledCenterY + y, 20, 24, 3, this.getBackgroundColor());
        RenderHelper.drawItem(context, this.getItem(), scaledCenterX + (x + 2), scaledCenterY +  (y + 1));
        String text = (cooldown / 20) + " сек";
        float textScale = scale / 4.44f;
        float textX = scaledCenterX + (x + (20 - RenderHelper.textRenderer.getWidth(text) * textScale) / 2);
        RenderHelper.drawCenteredYText(context, textX, scaledCenterY + (y + 14), textScale, text, Color.YELLOW);
        matrixStack.pop();
    }

    public void tick() {
        --cooldown;
    }

    public ItemStack getItem() {
        return Registries.ITEM.get(new Identifier(item)).getDefaultStack();
    }

    public void setItem(String item) {
        this.item = item;
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

//    @Override
//    public int hashCode() {
//        return Objects.hash(item);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        AbstractItemCooldown cooldownItem = (AbstractItemCooldown) obj;
//        return Objects.equals(this.item, cooldownItem.item);
//    }

}
