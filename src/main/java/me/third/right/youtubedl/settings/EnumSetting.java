package me.third.right.youtubedl.settings;

import com.google.gson.JsonPrimitive;
import lombok.Getter;
import me.third.right.youtubedl.manager.SettingsManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class EnumSetting<T extends Enum<?>> extends SettingBase {
    @Getter private final T[] values;
    @Getter private T selected;
    private final Function<EnumSetting<?>, ?> onReset;
    private final String toolTip;
    private final String resetToolTip;

    @Getter private JComboBox<T> component;

    public EnumSetting(String name,  Function<EnumSetting<?>, ?> onReset, String toolTip, String resetToolTip, T[] values, T selected) {
        super(name);
        this.values = values;
        this.selected = selected;
        this.onReset = onReset;
        this.toolTip = toolTip;
        this.resetToolTip = resetToolTip;
    }

    public void setSelected(T selected) {
        this.selected = selected;
        this.getComponent().setSelectedItem(this.getSelected());
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
    public void getComponents(JPanel panel, GridBagConstraints constraints) {
        final Label label = new Label();
        label.setText(this.getName());
        panel.add(label, constraints);

        component = new JComboBox<>(this.values);
        component.setBackground(Color.LIGHT_GRAY);
        component.addActionListener(X -> {
            setSelected(component.getItemAt(component.getSelectedIndex()));
            SettingsManager.INSTANCE.saveSettings();
        });
        component.setToolTipText(this.toolTip);
        panel.add(component, constraints);

        final JButton reset = new JButton("RESET");
        reset.setLayout(new FlowLayout(FlowLayout.RIGHT));
        reset.setPreferredSize(new  Dimension(80, 30));
        reset.setBackground(Color.LIGHT_GRAY);
        reset.addActionListener(X -> {
            onReset.apply(this);
        });
        reset.setToolTipText(resetToolTip);
        panel.add(reset, constraints);
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
