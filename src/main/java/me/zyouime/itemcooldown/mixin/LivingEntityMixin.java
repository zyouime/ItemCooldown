package me.zyouime.itemcooldown.mixin;

import me.zyouime.itemcooldown.util.UseItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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

    @Unique
    private ItemStack prevItem;

    @Inject(method = "consumeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.BEFORE))
    private void checkBeforeUse(CallbackInfo ci) {
        this.prevItem = activeItemStack.copy();
        System.out.println("SAVED!");
    }

    @Override
    public ItemStack getItem() {
        return this.prevItem;
    }
}
