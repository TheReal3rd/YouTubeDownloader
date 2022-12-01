package me.third.right.youtubedl;

import me.third.right.youtubedl.manager.CommandManager;
import me.third.right.youtubedl.manager.JFrameManager;

public class YTDL {

    private static boolean isWindows;

    public static void main(String[] args) {
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        CommandManager.INSTANCE = new CommandManager();
        JFrameManager.INSTANCE = new JFrameManager();
    }

    public static boolean isWindows() {
        return isWindows;
    }
}

