package me.third.right.youtubedl;

import me.third.right.youtubedl.manager.DownloadManager;
import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.utils.Utils;

public class YTDL {
    private static boolean isWindows;
    public final static String name = "YouTube Downloader";
    public final static String version = "1.3";

    public static void main(String[] args) {
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        DownloadManager.INSTANCE = new DownloadManager();
        JFrameManager.INSTANCE = new JFrameManager();
        System.out.println(Utils.mainPath.toString());
    }

    public static boolean isWindows() {
        return isWindows;
    }
}

