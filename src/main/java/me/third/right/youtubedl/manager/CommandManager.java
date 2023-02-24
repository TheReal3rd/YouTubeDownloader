package me.third.right.youtubedl.manager;

import me.third.right.youtubedl.runnables.DownloaderRunnable;
import me.third.right.youtubedl.utils.FormatEnum;

import static me.third.right.youtubedl.utils.Utils.downloaderCheck;
import static me.third.right.youtubedl.utils.Utils.ffmpegCheck;

public class CommandManager {
    // https://github.com/sapher/youtubedl-java
    //String.format("youtube-dl -x --audio-format %s %s".formatted(combo.toLowerCase(), link))
    // youtube-dl -x --audio-format mp3 https://www.youtube.com/watch?v=T0kpGEE-isU&list=RDMMeD81CsAFmDU&index=5d
    public static CommandManager INSTANCE;

    private boolean downloading = false;

    private Thread thread;

    private DownloaderRunnable downloaderRunnable;

    public void startDownload(String[] links, FormatEnum format) {
        downloading = true;

        if(!downloaderCheck()) {
            ErrorFrame frame = JFrameManager.INSTANCE.getErrorFrame();
            frame.setError("YouTube-DL needs to be extract wait a sec...");
            frame.setVisible(true);
            return;
        }

        if(!ffmpegCheck()) {

        }

        downloaderRunnable = new DownloaderRunnable(links, format);
        thread = new Thread(downloaderRunnable);
        thread.start();
    }

    public void stopDownload() {
        if(downloading) {
            downloading = false;
            downloaderRunnable.setStopping(true);
            thread.interrupt();
        }
    }


    public boolean isDownloading() {
        return downloading;
    }

    public Thread getThread() {
        return thread;
    }

    public DownloaderRunnable getDownloadRunnable() {
        return downloaderRunnable;
    }
}