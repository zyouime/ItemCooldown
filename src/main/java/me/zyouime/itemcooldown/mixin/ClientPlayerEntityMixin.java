package me.zyouime.itemcooldown.mixin;

import com.mojang.authlib.GameProfile;
import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.util.UseItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientPlayerEntity.class, priority = 500)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements UseItem {

    @Shadow public abstract boolean isUsingItem();

    @Unique
    private ItemStack prevItem;
    @Unique
    private ItemStack usageItem;
    @Unique
    private int usedSlot = -1;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public void swingHand(Hand hand, boolean fromServerPlayer) {
        if (ItemCooldown.getInstance().settings.enabled.getValue()) {
            ItemStack itemStack = this.getStackInHand(hand).copy();
            if (!itemStack.isEmpty() && itemStack.getItem() != Items.POTION && !itemStack.isFood()) {
                if (prevItem != null && !ItemStack.areItemsEqual(itemStack, prevItem)) {
                    setItem(itemStack);
                } else if (prevItem == null) {
                    setItem(itemStack);
                }
            }
        }
        super.swingHand(hand, fromServerPlayer);
    }

    @Inject(method = "dropSelectedItem", at = @At("RETURN"))
    private void dropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        clearIfNeed(this.getMainHandStack());
    }

    @Nullable
    @Override
    public ItemEntity dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership) {
        clearIfNeed(stack);
        return super.dropItem(stack, throwRandomly, retainOwnership);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
    private void onTick(CallbackInfo ci) {
        if (this.isUsingItem() && ItemStack.areItemsEqual(this.getStackInHand(this.getActiveHand()), this.activeItemStack) && ItemCooldown.getInstance().settings.enabled.getValue()) {
            ItemStack itemStack = this.activeItemStack.copy();
            if (usageItem != null) {
                if (ItemStack.areEqual(usageItem, itemStack)) {
                    setItem(itemStack);
                }
            } else {
                usageItem = itemStack;
                setItem(itemStack);
            }
        }
    }

    @Override
    public void stopUsingItem() {
        usageItem = null;
        super.stopUsingItem();
    }

    @Unique
    private void clearIfNeed(ItemStack stack) {
        if (prevItem != null && ItemStack.areItemsEqual(prevItem, stack)) {
            clearItem();
        }
    }

    @Unique
    private void setUsedItemSlot(ItemStack itemStack) {
        for (int i = 36; i <= Math.min(44, this.currentScreenHandler.slots.size() - 1); i++) {
            if (ItemStack.areEqual(this.currentScreenHandler.slots.get(i).getStack(), itemStack)) {
                usedSlot = i;
                return;
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return this.prevItem;
    }

    @Override
    public void setItem(ItemStack itemStack) {
        ItemCooldown.Settings settings = ItemCooldown.getInstance().settings;
        for (AbstractItemCooldown item : settings.items.getValue().get(settings.selectedCategory.getValue())) {
            if (ItemStack.areItemsEqual(item.getItem(), itemStack)) {
                this.prevItem = itemStack;
                setUsedItemSlot(itemStack);
                return;
            }
        }
    }

    @Override
    public void clearItem() {
        this.prevItem = null;
        this.usageItem = null;
        this.usedSlot = -1;
    }

    @Override
    public int getSlot() {
        return usedSlot;
    }
}
