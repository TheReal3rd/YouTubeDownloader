package me.third.right.youtubedl.gui;

import lombok.Getter;
import me.third.right.youtubedl.manager.SettingsManager;
import me.third.right.youtubedl.utils.YTFork;
import me.third.right.youtubedl.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class SettingsFrame {
    @Getter private final JFrame frame;

    @Getter private final JComboBox<YTFork> source;
    public SettingsFrame() {
        frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        final JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(480, 280));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        topPanel.setBackground(Color.lightGray);
        frame.add(topPanel);

        //TODO find the best way to link setting manager to the GUI. (Automatic)
        final Label textArea = new Label();
        textArea.setText("YTDL Fork");
        topPanel.add(textArea);

        source = new JComboBox<>(YTFork.values());
        source.setBackground(Color.LIGHT_GRAY);
        source.addActionListener(X -> {
            SettingsManager.INSTANCE.getSourceSetting().setSelected(source.getItemAt(source.getSelectedIndex()));
            SettingsManager.INSTANCE.saveSettings();
        });
        source.setToolTipText("Select YT-DL fork you wish to use.");
        topPanel.add(source);

        final JButton reset = new JButton("RESET");
        reset.setLayout(new FlowLayout(FlowLayout.RIGHT));
        reset.setPreferredSize(new  Dimension(80, 30));
        reset.setBackground(Color.LIGHT_GRAY);
        reset.addActionListener(X -> {

            for(YTFork x : YTFork.values()) {
                Utils.deleteFile(Utils.mainPath.resolve(x.getName()));
            }

            Utils.displayMessage("Done", "Deleted all downloaders.");
        });
        reset.setToolTipText("Deletes all YT Downloaders to redownload the latest versions.");
        topPanel.add(reset);

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
