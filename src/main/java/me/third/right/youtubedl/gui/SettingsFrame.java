package me.third.right.youtubedl.gui;

import lombok.Getter;
import me.third.right.youtubedl.manager.SettingsManager;
import me.third.right.youtubedl.utils.Source;

import javax.swing.*;
import java.awt.*;

public class SettingsFrame {
    @Getter private final JFrame frame;

    @Getter private final JComboBox<Source> source;
    public SettingsFrame() {
        frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        final JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(480, 280));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        topPanel.setBackground(Color.lightGray);
        frame.add(topPanel);

        source = new JComboBox<>(Source.values());
        source.setBackground(Color.LIGHT_GRAY);
        source.addActionListener(X -> {
            SettingsManager.INSTANCE.getSourceSetting().setSelected(source.getItemAt(source.getSelectedIndex()));
            SettingsManager.INSTANCE.saveSettings();
        });
        source.setToolTipText("Select YT-DL fork you wish to use.");
        topPanel.add(source);

        frame.pack();
        frame.setSize(490, 290);
        frame.setResizable(false);
        frame.setVisible(false);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public boolean isVisible() {
        return frame.isVisible();
    }
}
