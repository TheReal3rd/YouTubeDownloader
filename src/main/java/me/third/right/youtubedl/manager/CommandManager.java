package me.third.right.youtubedl.manager;

import lombok.Getter;
import me.third.right.youtubedl.runnables.DownloadYTRunnable;
import me.third.right.youtubedl.utils.FormatEnum;
import me.third.right.youtubedl.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import static me.third.right.youtubedl.utils.Utils.downloaderCheck;
import static me.third.right.youtubedl.utils.Utils.ffmpegCheck;

// https://github.com/sapher/youtubedl-java
//String.format("youtube-dl -x --audio-format %s %s".formatted(combo.toLowerCase(), link))
// youtube-dl -x --audio-format mp3 https://www.youtube.com/watch?v=T0kpGEE-isU&list=RDMMeD81CsAFmDU&index=5d
@Getter
public class CommandManager {
    public static CommandManager INSTANCE;

    private boolean downloading = false;

    private Thread thread;

    private DownloadYTRunnable downloadYTRunnable;

    public void startDownload(String[] links, FormatEnum format) {
        downloading = true;

        if(!downloaderCheck()) {//Works and complete.
            Utils.displayMessage("Missing Components","YouTube-DL needs to be downloaded wait a sec...");

            //Path
            Path path = Utils.mainPath.resolve("youtube-dl");
            //URL
            URL url;
            try {
                url = new URL("https://github.com/ytdl-patched/youtube-dl/releases/latest/");
                final String ytdlVersion = Utils.getGitLatest(url);
                url = new URL("https://github.com/ytdl-patched/youtube-dl/releases/download/%s/youtube-dl".formatted(ytdlVersion));
            } catch (IOException err) {
                Utils.displayMessage("YT-DL Latest Download Failure", "Something went wrong. { %s }".formatted(err));
                return;
            }

            Utils.downloadFile(path, url, true);//No need to put it in a thread.
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
}