package me.third.right.youtubedl.manager;

import me.third.right.youtubedl.runnables.DownloadRunnable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommandManager {
    // https://github.com/sapher/youtubedl-java
    //String.format("youtube-dl -x --audio-format %s %s".formatted(combo.toLowerCase(), link))
    // youtube-dl -x --audio-format mp3 https://www.youtube.com/watch?v=T0kpGEE-isU&list=RDMMeD81CsAFmDU&index=5d
    public static CommandManager INSTANCE;

    private boolean downloading = false;

    private Thread thread;

    private DownloadRunnable downloadRunnable;

    public void startDownload(String[] links, boolean extractAudio, String format) {
        downloading = true;

        if(!Files.exists(Paths.get(System.getProperty("user.dir")).resolve("youtube-dl"))) {
            extractTools();
        }

        downloadRunnable = new DownloadRunnable(links, extractAudio, format);
        thread = new Thread(downloadRunnable);
        thread.start();
    }

    public void stopDownload() {
        if(downloading) {
            downloading = false;
            downloadRunnable.setStopping(true);
            thread.interrupt();
        }
    }

    private void extractTools() {
        final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("youtube-dl");
        if(inputStream == null) {
            System.out.println("Failed to find the file.");
            return;
        }

        final File targetFile = new File("youtube-dl");
        try {
            final OutputStream outputStream = new FileOutputStream(targetFile);
            outputStream.write(inputStream.readAllBytes());

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDownloading() {
        return downloading;
    }

    public Thread getThread() {
        return thread;
    }

    public DownloadRunnable getDownloadRunnable() {
        return downloadRunnable;
    }
}