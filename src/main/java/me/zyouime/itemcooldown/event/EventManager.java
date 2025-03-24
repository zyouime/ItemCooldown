package me.zyouime.itemcooldown.event;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.util.CooldownManager;
import me.zyouime.itemcooldown.util.Wrapper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

public class EventManager implements Wrapper {

    private static long fightTimer;
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private static void hudRenderEvent() {
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            if (client.currentScreen instanceof ChatScreen) return;
            for (AbstractItemCooldown item : cooldownItems.get(ic.currentCategory)) {
                if (item.getCooldown() > 0) {
                    item.render(drawContext);
                }
            }
        }));
    }

    private static void tickEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (AbstractItemCooldown item : cooldownItems.get(ic.currentCategory)) {
                if (item.getCooldown() >= 0) item.tick();
                if (item.isResetWhenNoFightMode() && !isPvP()) {
                    item.setCooldown(0);
                }
            }
        });
    }
    
    private static void attackEntityEvent() {
        AttackEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
            if (entity instanceof PlayerEntity) {
                fightTimer = System.currentTimeMillis();
            }
            return ActionResult.PASS;
        }));
    }

    private static void joinEvent() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            for (AbstractItemCooldown item : cooldownItems.get(ic.currentCategory)) {
                item.setCooldown(0);
            }
        });
    }

    private static void useItemEvent() {
        UseItemCallback.EVENT.register(((player, world, hand) -> useItem(player, hand)));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> useItem(player, hand).getResult()));
    }

    public static void register() {
        hudRenderEvent();
        tickEvent();
        joinEvent();
        attackEntityEvent();
        useItemEvent();
    }

    public static boolean isPvP() {
        return (System.currentTimeMillis() - fightTimer) < 30000;
    }

    private static TypedActionResult<ItemStack> useItem(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isFood() && !itemStack.isOf(Items.POTION)) {
            CooldownManager.setCooldownIfNeeded(ic, itemStack);
        }
        return TypedActionResult.pass(itemStack);
    }
}
