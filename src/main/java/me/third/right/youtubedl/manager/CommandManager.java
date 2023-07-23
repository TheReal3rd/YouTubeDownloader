package me.third.right.youtubedl.manager;

import me.third.right.youtubedl.runnables.Download;
import me.third.right.youtubedl.runnables.DownloaderRunnable;
import me.third.right.youtubedl.utils.FormatEnum;
import me.third.right.youtubedl.utils.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
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

    private DownloaderRunnable downloaderRunnable;

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

                //TODO improve this it works tho so mission accomplished. in the future add a button to re-download this.
                //We're looking for href="/ytdl-patched/youtube-dl/releases/download/2023.07.23.114514/youtube-dl"
                //https://github.com/ytdl-patched/youtube-dl/releases/download/2023.07.23.114514/youtube-dl
                final HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    url = new URL(con.getURL().toString().replaceAll( "https://github.com/ytdl-patched/youtube-dl/releases/tag/","https://github.com/ytdl-patched/youtube-dl/releases/download/") + "/youtube-dl");
                    System.out.println(url);
                }
            } catch (IOException err) {
                Utils.displayError("Something went wrong. { %s }".formatted(err));
                return;
            }

            final Download download = new Download(path, url, true);
            thread = new Thread(download);
            thread.start();
            return;
        }

        if(!ffmpegCheck()) {
            //TODO implement this. (Not sure how yet)
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