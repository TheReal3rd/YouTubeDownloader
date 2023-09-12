package me.third.right.youtubedl.settings;

import com.google.gson.JsonPrimitive;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public abstract class SettingBase {
    @Getter private final String name;

    public SettingBase(String name) {
        this.name = name;
    }

    public abstract void getComponents(JPanel panel, GridBagConstraints constraints);

    public abstract void fromJson(JsonPrimitive object);

    public abstract JsonPrimitive toJson();
}
