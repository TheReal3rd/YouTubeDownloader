package me.third.right.youtubedl.manager;

import me.third.right.youtubedl.runnables.DownloadFileRunnable;
import me.third.right.youtubedl.runnables.DownloadYTRunnable;
import me.third.right.youtubedl.utils.FormatEnum;
import me.third.right.youtubedl.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static me.third.right.youtubedl.utils.Utils.downloaderCheck;
import static me.third.right.youtubedl.utils.Utils.ffmpegCheck;

public class CommandManager {
    // https://github.com/sapher/youtubedl-java
    //String.format("youtube-dl -x --audio-format %s %s".formatted(combo.toLowerCase(), link))
    // youtube-dl -x --audio-format mp3 https://www.youtube.com/watch?v=T0kpGEE-isU&list=RDMMeD81CsAFmDU&index=5d
    public static CommandManager INSTANCE;

    private boolean downloading = false;

    private Thread thread;

    private DownloadYTRunnable downloadYTRunnable;

    public void startDownload(String[] links, FormatEnum format) {
        downloading = true;

        if(!downloaderCheck()) {
            Utils.displayError("YouTube-DL needs to be downloaded wait a sec...");

            //Path
            Path path = Paths.get("youtube-dl");
            //URL
            URL url;
            try {
                url = new URL("https://github.com/ytdl-patched/youtube-dl/releases/latest/");
                final String ytdlVersion = Utils.getGitLatest(url);
                url = new URL("https://github.com/ytdl-patched/youtube-dl/releases/download/%s/youtube-dl".formatted(ytdlVersion));
            } catch (IOException err) {
                Utils.displayError("Something went wrong. { %s }".formatted(err));
                return;
            }

            final DownloadFileRunnable downloadFileRunnable = new DownloadFileRunnable(path, url, true);
            thread = new Thread(downloadFileRunnable);
            thread.start();
            return;
        }

        if(!ffmpegCheck()) {
            //TODO implement this. (Not sure how yet)
        }

        downloadYTRunnable = new DownloadYTRunnable(links, format);
        thread = new Thread(downloadYTRunnable);
        thread.start();
    }

    public void stopDownload() {
        if(downloading) {
            downloading = false;
            downloadYTRunnable.setStopping(true);
            thread.interrupt();
        }
    }


    public boolean isDownloading() {
        return downloading;
    }

    public Thread getThread() {
        return thread;
    }

    public DownloadYTRunnable getDownloadRunnable() {
        return downloadYTRunnable;
    }
}