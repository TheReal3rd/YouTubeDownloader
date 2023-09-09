package me.third.right.youtubedl.settings;

import com.google.gson.JsonPrimitive;
import lombok.Getter;

public abstract class SettingBase {
    @Getter private final String name;

    public SettingBase(String name) {
        this.name = name;
    }

    public abstract void fromJson(JsonPrimitive object);

    public abstract JsonPrimitive toJson();
}
