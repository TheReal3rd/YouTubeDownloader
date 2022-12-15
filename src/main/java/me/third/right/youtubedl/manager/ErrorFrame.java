package me.third.right.youtubedl.manager;

import com.sapher.youtubedl.YoutubeDLException;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class ErrorFrame {

    @Getter private final JFrame frame;

    @Getter
    private YoutubeDLException error = null;

    private final JTextArea message;

    public ErrorFrame() {
        frame = new JFrame("Error!");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        final JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(480, 280));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBackground(Color.lightGray);
        frame.add(topPanel);

        // Error message.
        message = new JTextArea();
        message.setBackground(Color.LIGHT_GRAY);
        message.setLineWrap(true);
        message.setEditable(false);
        message.setPreferredSize(new Dimension(480, 280));
        topPanel.add(message);

        frame.pack();
        frame.setSize(490, 290);
        frame.setResizable(false);
        frame.setVisible(false);
    }

    public void setError(YoutubeDLException error) {
        this.error = error;
        message.setText(error.getMessage());
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public boolean isVisible() {
        return frame.isVisible();
    }
}
