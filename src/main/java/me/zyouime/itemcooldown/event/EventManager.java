package me.zyouime.itemcooldown.event;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.mixin.BossBarHudAccessor;
import me.zyouime.itemcooldown.util.CooldownManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventManager {

    private static final ItemCooldown itemCooldown = ItemCooldown.getInstance();
    private static final String[] PVP_TYPES = new String[]{"режим боя", "пвп", "pvp"};
    private static final Map<ConfigData.Category, List<AbstractItemCooldown>> items = itemCooldown.settings.items.getValue();
    private static final ConfigData.Category selectedCategory = itemCooldown.settings.selectedCategory.getValue();

    private static void hudRenderEvent() {
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            if (MinecraftClient.getInstance().currentScreen instanceof ChatScreen) return;
            if (items.get(selectedCategory) == null) return;
            for (AbstractItemCooldown item : items.get(selectedCategory)) {
                if (item.getCooldown() > 0) {
                    item.render(drawContext);
                }
            }
        }));
    }

    private static void tickEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (items.get(selectedCategory) == null) return;
            for (AbstractItemCooldown item : items.get(selectedCategory)) {
                if (item.getCooldown() >= 0) item.tick();
                if (item.isResetWhenNoFightMode() && !isPvP()) {
                    item.setCooldown(0);
                }
            }
        });
    }

    private static void joinEvent() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (items.get(selectedCategory) == null) return;
            for (AbstractItemCooldown item : items.get(selectedCategory)) {
                item.setCooldown(0);
            }
        });
    }

    private static void useItemEvent() {
        UseItemCallback.EVENT.register(((player, world, hand) -> useItem(player, hand)));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> useItem(player, hand).getResult()));
    }

    public static void registerEvents() {
        hudRenderEvent();
        tickEvent();
        joinEvent();
        useItemEvent();
    }

    public static boolean isPvP() {
        BossBarHudAccessor bossBar = (BossBarHudAccessor) MinecraftClient.getInstance().inGameHud.getBossBarHud();
        for (Map.Entry<UUID, BossBar> entry : bossBar.getBossBars().entrySet()) {
            for (String pvpType : PVP_TYPES) {
                if (entry.getValue().getName().getString().toLowerCase().contains(pvpType)) return true;
            }
        }
        return false;
    }

    private static TypedActionResult<ItemStack> useItem(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isFood() && !itemStack.isOf(Items.POTION)) {
            CooldownManager.setCooldownIfNeeded(itemStack);
        }
        return TypedActionResult.pass(itemStack);
    }
}
