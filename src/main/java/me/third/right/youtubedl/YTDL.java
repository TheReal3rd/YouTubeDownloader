package me.third.right.youtubedl;

import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.manager.DownloadManager;
import me.third.right.youtubedl.manager.SettingsManager;
import me.third.right.youtubedl.utils.Utils;

public class YTDL {
    private static boolean isWindows;
    public final static String name = "YouTube Downloader";
    public final static String version = "1.4";

    public static void main(String[] args) {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");//Important.
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        DownloadManager.INSTANCE = new DownloadManager();
        JFrameManager.INSTANCE = new JFrameManager();
        SettingsManager.INSTANCE = new SettingsManager();
        SettingsManager.INSTANCE.loadSettings();
        System.out.println(Utils.mainPath.toString());
        Runtime.getRuntime().addShutdownHook(new shutHook());
    }

    public static boolean isWindows() {
        return isWindows;
    }
}

