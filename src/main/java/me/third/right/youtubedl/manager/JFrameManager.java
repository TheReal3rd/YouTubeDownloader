package me.third.right.youtubedl.manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JFrameManager {
    public static JFrameManager INSTANCE;

    private final JFrame frame;

    private final JButton start;
    private final JButton cancel;
    private final JButton clear;

    private final JLabel progress;

    private final JTextArea links;

    private final JCheckBox downloadPlaylists;

    private final JCheckBox extractAudio;
    private final String[] videoFormats = new String[] { "MP4", "MKV" };
    private final JComboBox<String> videoFormat;
    private final String[] audioFormats = new String[] { "MP3", "M4A" };
    private final JComboBox<String> audioFormat;

    private final JButton debug;

    public JFrameManager() {
        frame = new JFrame(" YTDL V1 ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new FlowLayout());
        secondPanel.setBackground(Color.BLACK);
        frame.add(secondPanel);

        // Text Area

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(920, 440));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        topPanel.setBackground(Color.LIGHT_GRAY);
        secondPanel.add(topPanel);

        links = new JTextArea();
        links.setPreferredSize(new Dimension(921, 441));
        links.setBackground(Color.LIGHT_GRAY);
        links.setCaretColor(Color.MAGENTA);
        topPanel.add(links);

        // Button Area

        start = new JButton("START");
        start.setLayout(new FlowLayout(FlowLayout.LEFT));
        start.setPreferredSize(new  Dimension(90, 40));
        start.setBackground(Color.LIGHT_GRAY);
        start.addActionListener(X -> {
            final boolean extractAudio = getExtractAudio().isSelected();
            final String format = extractAudio ?  audioFormats[getAudioFormat().getSelectedIndex()] : videoFormats[getVideoFormat().getSelectedIndex()];
            CommandManager.INSTANCE.startDownload(links.getText().split("\n"), extractAudio, format);
        });
        secondPanel.add(start);

        cancel = new JButton("CANCEL");
        cancel.setLayout(new FlowLayout(FlowLayout.LEFT));
        cancel.setPreferredSize(new  Dimension(90, 40));
        cancel.setBackground(Color.LIGHT_GRAY);
        cancel.addActionListener(X -> {
            CommandManager.INSTANCE.stopDownload();
        });
        secondPanel.add(cancel);

        clear = new JButton("CLEAR");
        clear.setLayout(new FlowLayout(FlowLayout.RIGHT));
        clear.setPreferredSize(new  Dimension(90, 40));
        clear.setBackground(Color.LIGHT_GRAY);
        clear.addActionListener(X -> links.setText(""));
        secondPanel.add(clear);

        // Dropdown Area

        downloadPlaylists = new JCheckBox("Download Playlists", false);
        downloadPlaylists.setBackground(Color.BLACK);
        secondPanel.add(downloadPlaylists);

        extractAudio = new JCheckBox("Extract Audio", false);
        extractAudio.setBackground(Color.BLACK);
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
        audioFormat.setBackground(Color.LIGHT_GRAY);
        secondPanel.add(audioFormat);

        videoFormat = new JComboBox<>(videoFormats);
        videoFormat.setVisible(!extractAudio.isSelected());
        videoFormat.setBackground(Color.LIGHT_GRAY);
        secondPanel.add(videoFormat);

        // Progress and logs

        progress = new JLabel("Progress: 0.0% ?/?");
        progress.setBackground(Color.BLACK);
        secondPanel.add(progress);

        //Debug

        debug = new JButton("Debug");
        debug.addActionListener(X -> {

        });
        //secondPanel.add(debug);

        // Frame END
        frame.pack();
        frame.setSize(960, 540);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setProgress(float progressF, int size, int index) {
        progress.setText("Progress: %.1f%% %d/%d".formatted(progressF, index, size));
    }

    public JCheckBox getExtractAudio() {
        return extractAudio;
    }

    public JComboBox<String> getAudioFormat() {
        return audioFormat;
    }

    public JComboBox<String> getVideoFormat() {
        return videoFormat;
    }

    public JCheckBox getDownloadPlaylists() {
        return downloadPlaylists;
    }
}
