package me.third.right.youtubedl.gui;

import lombok.Getter;
import me.third.right.youtubedl.manager.SettingsManager;
import me.third.right.youtubedl.settings.SettingBase;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SettingsFrame {
    @Getter private final JFrame frame;

    public SettingsFrame() {
        frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        final JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(480, 280));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        topPanel.setBackground(Color.lightGray);
        frame.add(topPanel);

        final JPanel midPanel = new JPanel(new GridBagLayout());
        midPanel.setBackground(Color.lightGray);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        int yGrid = 0;
        for (Map.Entry<String, SettingBase> entry : SettingsManager.INSTANCE.getSettingsMap().entrySet()) {
            constraints.gridy = yGrid;
            entry.getValue().getComponents(midPanel, constraints);
            yGrid++;
        }
        constraints.anchor = GridBagConstraints.CENTER;
        midPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Settings: "));

        topPanel.add(midPanel, constraints);

        frame.pack();
        frame.setSize(490, 290);
        frame.setResizable(false);
        frame.setVisible(false);
        frame.setLocationRelativeTo(null);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public boolean isVisible() {
        return frame.isVisible();
    }
}
