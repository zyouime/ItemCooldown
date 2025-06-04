package me.zyouime.itemcooldown.mixin;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.util.UseItem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements UseItem {

    @Shadow
    protected ItemStack activeItemStack;
    @Shadow public abstract ItemStack getStackInHand(Hand hand);
    @Unique
    private ItemStack prevItem;
    @Unique
    private LivingEntity livingEntity = (LivingEntity) (Object) this;

    @Inject(method = "consumeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.BEFORE))
    private void consumeItem(CallbackInfo ci) {
        if (isClientPlayerAndIfEnabled()) {
            this.prevItem = this.activeItemStack.copy();
        }
    }

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At("RETURN"))
    private void swingHand(Hand hand, CallbackInfo ci) {
        if (isClientPlayerAndIfEnabled()) {
            this.prevItem = this.getStackInHand(hand).copy();
        }
    }

    @Unique
    private boolean isClientPlayerAndIfEnabled() {
        return livingEntity instanceof ClientPlayerEntity && ItemCooldown.getInstance().settings.enabled.getValue();
    }

    @Override
    public ItemStack getItem() {
        return this.prevItem;
    }

    @Override
    public void clearItem() {
        this.prevItem = null;
    }
}
