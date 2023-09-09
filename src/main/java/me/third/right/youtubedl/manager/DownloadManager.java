package me.third.right.youtubedl.manager;

import lombok.Getter;
import lombok.Setter;
import me.third.right.youtubedl.YTDL;
import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.settings.EnumSetting;
import me.third.right.youtubedl.utils.FormatEnum;
import me.third.right.youtubedl.utils.Source;
import me.third.right.youtubedl.utils.Utils;
import me.third.right.youtubedl.utils.runnables.DownloadM3U8Runnable;
import me.third.right.youtubedl.utils.runnables.DownloadYTRunnable;
import me.third.right.youtubedl.utils.runnables.RunnableBase;

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
            EnumSetting<Source> setting = SettingsManager.INSTANCE.getSourceSetting();

            //Path
            Path path = Utils.mainPath.resolve(setting.getSelected().getName());
            //URL
            URL url;
            try {
                url = new URL("%s/releases/latest/".formatted(setting.getSelected().getUrl()));
                final String ytdlVersion = Utils.getGitLatest(url);
                if(ytdlVersion == null) {
                    Utils.displayMessage("Failed to fetch version", "The process to fetch the latest version of the requested YTDL fork failed.");
                    System.out.println("\n\n%s\n\n".formatted(url.toString()));
                    System.out.println("Its null.");
                    return;
                }
                url = new URL("%s/releases/download/%s/%s".formatted(setting.getSelected().getUrl(), ytdlVersion, setting.getSelected().getName()));
            } catch (IOException err) {
                Utils.displayMessage("YT-DL Latest Download Failure", "Something went wrong. { %s }".formatted(err));
                System.out.println(err);
                return;
            }

            System.out.println("\n\n%s\n\n".formatted(url.toString()));

            Utils.downloadFile(path, url, true);//No need to put it in a thread.
        }

        //TODO check winget has ffmpeg install.
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