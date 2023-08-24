package me.third.right.youtubedl.manager;

import lombok.Getter;
import lombok.Setter;
import me.third.right.youtubedl.YTDL;
import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.utils.runnables.DownloadM3U8Runnable;
import me.third.right.youtubedl.utils.runnables.DownloadYTRunnable;
import me.third.right.youtubedl.utils.runnables.RunnableBase;
import me.third.right.youtubedl.utils.FormatEnum;
import me.third.right.youtubedl.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import static me.third.right.youtubedl.utils.Utils.downloaderCheck;
import static me.third.right.youtubedl.utils.Utils.ffmpegCheck;

@Getter
public class DownloadManager {
    public static DownloadManager INSTANCE;

    private boolean downloading = false;

    private Thread thread;

    private RunnableBase downloadRunnable;
    @Setter private Process process;

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

        //I would make it download ffmpeg then extract it then do a cleanup. But honestly no. Windows is the main annoyance. unlike windows ffmpeg can be installed using apt or pacman.
        //So fuck windows slow, bloated and a memory hog.
        if(!ffmpegCheck()) {//I won't make this download the latest version from git cause FFMPEG never really updates.
            if(YTDL.isWindows()) {//Windows makes me want to die. Will I fix this? No.
                String[] names = { "ffmpeg.exe", "avcodec-60.dll", "avdevice-60.dll", "avfilter-9.dll", "avutil-58.dll", "avformat-60.dll", "postproc-57.dll", "swresample-4.dll", "swscale-7.dll" };
                for(String name : names) {
                    Utils.extractResource("winFFmpeg/%s".formatted(name), Utils.mainPath.resolve(name));
                }
            }
        }

        if(JFrameManager.INSTANCE.getDownloadM3U8().isSelected()) {
            downloadRunnable = new DownloadM3U8Runnable(links);
        } else {
            downloadRunnable = new DownloadYTRunnable(links, format);
        }
        thread = new Thread(downloadRunnable);
        thread.start();
    }

    public void stopDownload() {
        if(downloading) {
            process.destroyForcibly();
            downloading = false;
            downloadRunnable.setStopping(true);
            thread.interrupt();
        }
    }
}