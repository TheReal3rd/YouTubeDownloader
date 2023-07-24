package me.third.right.youtubedl.manager;

import lombok.Getter;
import me.third.right.youtubedl.YTDL;
import me.third.right.youtubedl.utils.FormatEnum;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * The GUI Interface used to control YouTube DL
 */
public class JFrameManager {
    public static JFrameManager INSTANCE;

    @Getter private final JTextArea links;
    private final JLabel progress;

    @Getter private final JCheckBox downloadPlaylists;

    @Getter private final JComboBox<FormatEnum> format;

    private final FileSelectFrame fileSelectFrame = new FileSelectFrame();

    @Getter private final ErrorFrame errorFrame = new ErrorFrame();

    public JFrameManager() {
        JFrame frame = new JFrame("%s %s".formatted(YTDL.name, YTDL.version));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            final URL resource = JFrameManager.class.getResource("/Logo.png");
            if(resource != null) {
                final BufferedImage image = ImageIO.read(resource);
                frame.setIconImage(image);
            }
        } catch (IOException ignore) {
            //If it doesn't work no matter it's not important.
        }

        final JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new FlowLayout());
        secondPanel.setBackground(Color.BLACK);
        frame.add(secondPanel);

        // Text Area

        final JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(920, 440));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        topPanel.setBackground(Color.LIGHT_GRAY);
        secondPanel.add(topPanel);

        links = new JTextArea();
        links.setBackground(Color.LIGHT_GRAY);

        final JScrollPane scrollPane = new JScrollPane(links);
        scrollPane.setPreferredSize(new Dimension(921, 441));
        topPanel.add(scrollPane);

        // Button Area

        final JButton clear = new JButton("CLEAR");
        clear.setLayout(new FlowLayout(FlowLayout.RIGHT));
        clear.setPreferredSize(new  Dimension(80, 30));
        clear.setBackground(Color.LIGHT_GRAY);
        clear.addActionListener(X -> links.setText(""));
        secondPanel.add(clear);

        final JButton start = new JButton("START");
        start.setLayout(new FlowLayout(FlowLayout.LEFT));
        start.setPreferredSize(new  Dimension(80, 30));
        start.setBackground(Color.LIGHT_GRAY);
        start.addActionListener(X -> {
            CommandManager.INSTANCE.startDownload(links.getText().split("\n"), FormatEnum.values()[getFormat().getSelectedIndex()]);
        });
        secondPanel.add(start);

        final JButton cancel = new JButton("CANCEL");
        cancel.setLayout(new FlowLayout(FlowLayout.LEFT));
        cancel.setPreferredSize(new  Dimension(90, 30));
        cancel.setBackground(Color.LIGHT_GRAY);
        cancel.addActionListener(X -> CommandManager.INSTANCE.stopDownload());
        secondPanel.add(cancel);

        final JButton fileSelect = new JButton("File");
        fileSelect.setLayout(new FlowLayout(FlowLayout.RIGHT));
        fileSelect.setPreferredSize(new  Dimension(60, 30));
        fileSelect.setBackground(Color.LIGHT_GRAY);
        fileSelect.addActionListener(X -> fileSelectFrame.setVisible(!fileSelectFrame.isVisible()));
        secondPanel.add(fileSelect);

        // Dropdown Area

        downloadPlaylists = new JCheckBox("Download Playlists", false);
        downloadPlaylists.setBackground(Color.BLACK);
        downloadPlaylists.setForeground(Color.WHITE);
        secondPanel.add(downloadPlaylists);

        format = new JComboBox<>(FormatEnum.values());
        format.setBackground(Color.LIGHT_GRAY);
        secondPanel.add(format);

        // Progress and logs

        progress = new JLabel("Progress: 0.0% ETA: 0s ?/?");
        progress.setBackground(Color.BLACK);
        progress.setForeground(Color.WHITE);
        secondPanel.add(progress);

        // Frame END
        frame.pack();
        frame.setSize(960, 520);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setProgress(float progress, long eta, int completed, int amount) {
        this.progress.setText("Progress: %.1f%% ETA: %ds %d/%d".formatted(progress, eta, completed, amount));
    }
}
