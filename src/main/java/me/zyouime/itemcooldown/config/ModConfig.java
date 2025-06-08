package me.zyouime.itemcooldown.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.zyouime.itemcooldown.config.adapter.ItemStackTypeAdapter;
import me.zyouime.itemcooldown.config.adapter.NbtCompoundTypeAdapter;
import me.zyouime.itemcooldown.config.adapter.RuntimeTypeAdapterFactory;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.item.CustomItemCooldown;
import me.zyouime.itemcooldown.item.VanillaItemCooldown;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {

    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "itemcooldown.json");
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(NbtCompound.class, new NbtCompoundTypeAdapter())
            .registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter())
            .registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                    .of(AbstractItemCooldown.class)
                    .registerSubtype(VanillaItemCooldown.class)
                    .registerSubtype(CustomItemCooldown.class))
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    public static ConfigData configData;

    public static void loadConfig() {
        try (FileReader fileReader = new FileReader(FILE)) {
            ConfigData configData1 = GSON.fromJson(fileReader, ConfigData.class);
            if (configData1 != null) {
                configData = configData1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveConfig() {
        try (FileWriter fileWriter = new FileWriter(FILE)) {
            GSON.toJson(configData, fileWriter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void initialize() {
        try {
            if (!FILE.exists()) {
                FILE.createNewFile();
                configData = new ConfigData();
                saveConfig();
            } else loadConfig();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить конфиг, ошибка: " + e);
        }
    }
}
