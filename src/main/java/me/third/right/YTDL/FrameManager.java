package me.third.right.YTDL;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FrameManager {
    public static FrameManager INSTANCE;

    @Getter private final JFrame frame;

    @Getter private final JButton start;
    @Getter private final JButton cancel;
    @Getter private final JButton clear;

    @Getter private final JLabel progress;

    @Getter private final JTextArea links;

    @Getter private final JCheckBox downloadPlaylists;

    @Getter private final JCheckBox extractAudio;
    @Getter private final String[] videoFormats = new String[] { "MP4", "MKV" };
    @Getter private final JComboBox<String> videoFormat;
    @Getter private final String[] audioFormats = new String[] { "MP3", "M4A" };
    @Getter private final JComboBox<String> audioFormat;

    public FrameManager() {//TODO move to JavaFX for better styling.
        frame = new JFrame(" YTDL V1 ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new FlowLayout());
        secondPanel.setBackground(Color.gray);
        frame.add(secondPanel);

        // Text Area

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(920, 440));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        topPanel.setBackground(Color.WHITE);
        secondPanel.add(topPanel);

        links = new JTextArea();
        links.setPreferredSize(new Dimension(921, 441));
        topPanel.add(links);

        // Button Area

        start = new JButton("START");
        start.setLayout(new FlowLayout(FlowLayout.LEFT));
        start.setPreferredSize(new  Dimension(90, 40));
        start.addActionListener(X -> {
            final boolean extractAudio = getExtractAudio().isSelected();
            final String format = extractAudio ?  audioFormats[getAudioFormat().getSelectedIndex()] : videoFormats[getVideoFormat().getSelectedIndex()];
            CommandManager.INSTANCE.startDownload(links.getText().split("\n"), extractAudio, format);
        });
        secondPanel.add(start);

        cancel = new JButton("CANCEL");
        cancel.setLayout(new FlowLayout(FlowLayout.LEFT));
        cancel.setPreferredSize(new  Dimension(90, 40));
        cancel.addActionListener(X -> {
            CommandManager.INSTANCE.stopDownload();
        });
        secondPanel.add(cancel);

        clear = new JButton("CLEAR");
        clear.setLayout(new FlowLayout(FlowLayout.RIGHT));
        clear.setPreferredSize(new  Dimension(90, 40));
        clear.addActionListener(X -> links.setText(""));
        secondPanel.add(clear);

        // Dropdown Area

        downloadPlaylists = new JCheckBox("Download Playlists", false);
        secondPanel.add(downloadPlaylists);

        extractAudio = new JCheckBox("Extract Audio", false);
        extractAudio.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audioFormat.setVisible(extractAudio.isSelected());
                videoFormat.setVisible(!extractAudio.isSelected());
            }
        });
        secondPanel.add(extractAudio);

        audioFormat = new JComboBox<>(audioFormats);
        audioFormat.setVisible(extractAudio.isSelected());
        secondPanel.add(audioFormat);

        videoFormat = new JComboBox<>(videoFormats);
        videoFormat.setVisible(!extractAudio.isSelected());
        secondPanel.add(videoFormat);

        // Progress and logs

        progress = new JLabel("Progress: 0.0%");
        secondPanel.add(progress);

        // Frame END
        frame.pack();
        frame.setSize(960, 540);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setProgress(float progressF) {
        progress.setText("Progress: %.1f%%".formatted(progressF));
    }

}
