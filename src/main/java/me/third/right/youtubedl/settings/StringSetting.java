package me.third.right.youtubedl.settings;

import com.google.gson.JsonPrimitive;
import lombok.Getter;
import me.third.right.youtubedl.manager.SettingsManager;

import javax.swing.*;
import java.awt.*;

public class StringSetting extends SettingBase {
    @Getter private String value;
    private final String defaultVar;

    private TextField component;
    private final String resetToolTip;

    public StringSetting(String name, String value, String resetToolTip) {
        super(name);
        this.value = value;
        this.defaultVar = value;
        this.resetToolTip = resetToolTip;
    }

    private void setValue(String value) {
        this.value = value;
        this.component.setText(this.getValue());
    }

    private void reset() {
        setValue(defaultVar);
    }

    @Override
    public void getComponents(JPanel panel, GridBagConstraints constraints) {
        final Label label = new Label();
        label.setText(this.getName());
        panel.add(label, constraints);

        component = new TextField();
        component.setBackground(Color.LIGHT_GRAY);
        component.addTextListener(e -> {
            setValue(component.getText());
            SettingsManager.INSTANCE.saveSettings();
        });
        component.setText(this.value);
        component.setColumns(20);
        panel.add(component, constraints);

        final JButton reset = new JButton("RESET");
        reset.setLayout(new FlowLayout(FlowLayout.RIGHT));
        reset.setPreferredSize(new  Dimension(80, 30));
        reset.setBackground(Color.LIGHT_GRAY);
        reset.addActionListener(X -> this.reset());
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

        setValue(primitive.getAsString());
    }

    @Override
    public JsonPrimitive toJson() {
        return new JsonPrimitive(this.value);
    }
}
