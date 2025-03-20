package me.zyouime.itemcooldown.item;


import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.util.CooldownRenderable;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import me.zyouime.itemcooldown.config.ConfigData;

import java.awt.*;
import java.util.Objects;


public class ItemCooldownStatus {

    private final Item item;
    private int cooldown;
    private float x, y;

    public ItemCooldownStatus(Item item, int cooldown) {
        this.item = item;
        this.cooldown = cooldown;
    }

    public void tick() {
        --cooldown;
    }

    public void render(DrawContext context) {
        MatrixStack matrixStack = context.getMatrices();
        ConfigData configData = ModConfig.configData;
        float scale = (float) configData.getField("scale");
        ItemStack renderItem = this.getItem().getDefaultStack();
        Color background = new Color(0, 0, 0, 96);
        matrixStack.push();
        matrixStack.scale(scale, scale, 1f);
        RenderHelper.drawRoundedRect(matrixStack, x, y, 20, 23, 3, background);
        RenderHelper.drawItem(context, renderItem, x + 2, y + 1);
        String text = (cooldown / 20) + " сек";
        float textScale = scale / 4.44f;
        float textX = x + (20 - RenderHelper.textRenderer.getWidth(text) * textScale) / 2;
        RenderHelper.drawCenteredYText(context, textX, y + 13, textScale, text, Color.YELLOW);
        matrixStack.pop();
    }

    public int getCooldown() {
        return cooldown;
    }

    public void updatePos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Item getItem() {
        return item;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemCooldownStatus cooldownItem = (ItemCooldownStatus) obj;
        return Objects.equals(this.item, cooldownItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }
}
