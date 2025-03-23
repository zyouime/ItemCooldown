package me.zyouime.itemcooldown.mixin;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private final ItemCooldown ic = ItemCooldown.getInstance();
    @Unique
    private float offsetX, offsetY;
    @Unique
    private float scale, scaledCenterX, scaledCenterY, itemWidth, itemHeight;
    @Unique
    private boolean isDragging;

    @Unique
    private AbstractItemCooldown draggingItem;

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        scale = (float) ModConfig.configData.getField("scale");
        Window window = this.client.getWindow();
        scaledCenterX = window.getScaledWidth() / 2f;
        scaledCenterY = window.getScaledHeight() / 2f;
        itemWidth = 20 * scale;
        itemHeight = 24 * scale;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ic.cooldownItems().get(ic.currentCategory).forEach(aic -> aic.render(context));
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (AbstractItemCooldown item : ic.cooldownItems().get(ic.currentCategory)) {
            float itemX = scaledCenterX + item.getX() * scale;
            float itemY = scaledCenterY + item.getY() * scale;
            if (mouseX >= itemX && mouseX <= itemX + itemWidth && mouseY >= itemY && mouseY <= itemY + itemHeight) {
                isDragging = true;
                offsetX = (float) mouseX - itemX;
                offsetY = (float) mouseY - itemY;
                draggingItem = item;
                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && draggingItem != null) {
            float x = (float) mouseX - scaledCenterX - offsetX;
            float y = (float) mouseY - scaledCenterY - offsetY;
            x = Math.max(-scaledCenterX, Math.min(x, (scaledCenterX * 2) - scaledCenterX - itemWidth));
            y = Math.max(-scaledCenterY, Math.min(y, (scaledCenterY * 2) - scaledCenterY - itemHeight));
            draggingItem.setX(x / scale);
            draggingItem.setY(y / scale);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        draggingItem = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

}
