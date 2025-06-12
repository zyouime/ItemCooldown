package me.zyouime.itemcooldown.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "isMultiplayerEnabled", at = @At("HEAD"), cancellable = true)
    public void isMultiplayerEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "isOnlineChatEnabled", at = @At("HEAD"), cancellable = true)
    public void isOnlineChatEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
