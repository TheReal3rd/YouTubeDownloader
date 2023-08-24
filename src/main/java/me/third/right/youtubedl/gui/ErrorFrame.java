package me.third.right.youtubedl.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class ErrorFrame {

    @Getter private final JFrame frame;

    private final JTextArea message;

    public ErrorFrame() {
        frame = new JFrame("Error!");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        final JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(480, 280));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        topPanel.setBackground(Color.lightGray);
        frame.add(topPanel);

        // Error message.
        message = new JTextArea();
        message.setBackground(Color.LIGHT_GRAY);
        message.setLineWrap(true);
        message.setEditable(false);
        message.setPreferredSize(new Dimension(440, 240));
        topPanel.add(message);

        frame.pack();
        frame.setSize(490, 290);
        frame.setResizable(false);
        frame.setVisible(false);
    }

    public void setMessage(String error) {
        message.setText(error);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public boolean isVisible() {
        return frame.isVisible();
    }
}
