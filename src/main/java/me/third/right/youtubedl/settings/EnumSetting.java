package me.third.right.youtubedl.settings;

import com.google.gson.JsonPrimitive;
import lombok.Getter;

import java.util.function.Function;

public class EnumSetting<T extends Enum<?>> extends SettingBase {
    @Getter private final T[] values;
    @Getter private T selected;
    private final Function<EnumSetting<?>, ?> onSet;

    public EnumSetting(String name, Function<EnumSetting<?>, ?> onSet, T[] values, T selected) {
        super(name);
        this.values = values;
        this.selected = selected;
        this.onSet = onSet;
    }

    public void setSelected(T selected) {
        this.selected = selected;
        this.onSet.apply(this);
    }

    public void setSelected(String selected) {
        for(T value : values) {
            if(!value.toString().equalsIgnoreCase(selected))
                continue;

            setSelected(value);
            break;
        }
    }

    @Override
    public void fromJson(JsonPrimitive object) {
        if(!object.isJsonPrimitive())
            return;

        JsonPrimitive primitive = object.getAsJsonPrimitive();
        if(!primitive.isString())
            return;

        setSelected(primitive.getAsString());
    }

    @Override
    public JsonPrimitive toJson() {
        return new JsonPrimitive(selected.toString());
    }
}
