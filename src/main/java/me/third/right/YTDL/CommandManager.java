package me.third.right.YTDL;

import lombok.Getter;

public class CommandManager { // https://github.com/sapher/youtubedl-java

    //String.format("youtube-dl -x --audio-format %s %s".formatted(combo.toLowerCase(), link))
    public static CommandManager INSTANCE;

    @Getter
    private boolean downloading = false;

    @Getter
    private Thread thread;

    @Getter
    private DownloadRunnable downloadRunnable;

    public void startDownload(String[] links, boolean extractAudio, String format) {
        downloading = true;
        downloadRunnable = new DownloadRunnable(links, extractAudio, format);
        thread = new Thread(downloadRunnable);
        thread.start();
        downloading = false;
    }

    public void stopDownload() {
        downloading = false;
        downloadRunnable.setStopping(true);
        thread.interrupt();
    }
}
// youtube-dl -x --audio-format mp3 https://www.youtube.com/watch?v=T0kpGEE-isU&list=RDMMeD81CsAFmDU&index=5d