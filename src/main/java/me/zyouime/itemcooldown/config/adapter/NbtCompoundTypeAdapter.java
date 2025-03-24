package me.zyouime.itemcooldown.config.adapter;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

import java.lang.reflect.Type;

public class NbtCompoundTypeAdapter implements JsonSerializer<NbtCompound>, JsonDeserializer<NbtCompound> {

    @Override
    public JsonElement serialize(NbtCompound src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.asString());
    }

    @Override
    public NbtCompound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return StringNbtReader.parse(json.getAsString());
        } catch (CommandSyntaxException e) {
            return new NbtCompound();
        }
    }

}
