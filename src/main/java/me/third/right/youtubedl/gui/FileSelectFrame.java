package me.third.right.youtubedl.gui;

import lombok.Getter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Paths;

import static me.third.right.youtubedl.utils.Utils.loadTextFile;

public class FileSelectFrame {

    @Getter private final JFrame frame;

    @Getter private final JFileChooser fileChooser;

    public FileSelectFrame() {
        frame = new JFrame("File Select");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(960, 500));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        topPanel.setBackground(Color.lightGray);
        frame.add(topPanel);

        // File selection
        fileChooser = new JFileChooser(Paths.get(System.getProperty("user.dir")).toFile());
        fileChooser.setBackground(Color.lightGray);
        fileChooser.setPreferredSize(new Dimension(960, 500));
        fileChooser.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                    case "ApproveSelection" -> {
                        JFrameManager.INSTANCE.getLinks().setText(loadTextFile( fileChooser.getSelectedFile()));
                        setVisible(false);
                    }
                    case "CancelSelection" -> setVisible(false);
                }
            }
        });
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        topPanel.add(fileChooser);

        frame.pack();
        frame.setSize(960, 520);
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
