package me.third.right.youtubedl.manager;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class JFrameManager {
    public static JFrameManager INSTANCE;

    @Getter private final JFrame frame;

    @Getter private final JButton start;
    @Getter private final JButton cancel;
    @Getter private final JButton clear;
    @Getter private final JButton fileSelect;

    @Getter private final JLabel progress;

    @Getter private final JTextArea links;

    @Getter private final JCheckBox downloadPlaylists;

    @Getter private final JCheckBox extractAudio;
    @Getter private final String[] videoFormats = new String[] { "MP4", "MKV" };
    @Getter private final JComboBox<String> videoFormat;
    @Getter private final String[] audioFormats = new String[] { "MP3", "M4A" };
    @Getter private final JComboBox<String> audioFormat;

    @Getter private final FileSelectFrame fileSelectFrame = new FileSelectFrame();

    @Getter private final ErrorFrame errorFrame = new ErrorFrame();

    public JFrameManager() {
        frame = new JFrame("YouTube Downloader V1.1");
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
        links.setBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(links);
        scrollPane.setPreferredSize(new Dimension(921, 441));
        topPanel.add(scrollPane);

        // Button Area

        clear = new JButton("CLEAR");
        clear.setLayout(new FlowLayout(FlowLayout.RIGHT));
        clear.setPreferredSize(new  Dimension(80, 30));
        clear.setBackground(Color.LIGHT_GRAY);
        clear.addActionListener(X -> links.setText(""));
        secondPanel.add(clear);

        start = new JButton("START");
        start.setLayout(new FlowLayout(FlowLayout.LEFT));
        start.setPreferredSize(new  Dimension(80, 30));
        start.setBackground(Color.LIGHT_GRAY);
        start.addActionListener(X -> {
            final boolean extractAudio = getExtractAudio().isSelected();
            final String format = extractAudio ?  audioFormats[getAudioFormat().getSelectedIndex()] : videoFormats[getVideoFormat().getSelectedIndex()];
            CommandManager.INSTANCE.startDownload(links.getText().split("\n"), extractAudio, format);
        });
        secondPanel.add(start);

        cancel = new JButton("CANCEL");
        cancel.setLayout(new FlowLayout(FlowLayout.LEFT));
        cancel.setPreferredSize(new  Dimension(90, 30));
        cancel.setBackground(Color.LIGHT_GRAY);
        cancel.addActionListener(X -> CommandManager.INSTANCE.stopDownload());
        secondPanel.add(cancel);

        fileSelect = new JButton("File");
        fileSelect.setLayout(new FlowLayout(FlowLayout.RIGHT));
        fileSelect.setPreferredSize(new  Dimension(60, 30));
        fileSelect.setBackground(Color.LIGHT_GRAY);
        fileSelect.addActionListener(X -> fileSelectFrame.setVisible(!fileSelectFrame.isVisible()));
        secondPanel.add(fileSelect);

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

        // Frame END
        frame.pack();
        frame.setSize(960, 520);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setProgress(float progressF, int size, int index) {
        progress.setText("Progress: %.1f%% %d/%d".formatted(progressF, index, size));
    }

    public void loadFile(File file) {
        final StringBuilder messages = new StringBuilder();
        final InputStream stream;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        final Scanner scanner = new Scanner(stream);
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if(line.isEmpty()) continue;
            messages.append(line).append("\n");
        }

        scanner.close();
        getLinks().setText(messages.toString());
    }
}
