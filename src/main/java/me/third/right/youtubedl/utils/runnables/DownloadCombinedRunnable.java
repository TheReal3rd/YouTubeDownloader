package me.third.right.youtubedl.utils.runnables;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.utils.FormatEnum;
import me.third.right.youtubedl.utils.Utils;
import me.third.right.youtubedl.utils.m3u8.m3u8;
import me.third.right.youtubedl.utils.m3u8.m3u8Request;
import me.third.right.youtubedl.utils.m3u8.m3u8Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static me.third.right.youtubedl.utils.Utils.mainPath;

public class DownloadCombinedRunnable extends RunnableBase {
    private FormatEnum format;
    private final String[] links;
    private int counter = 1;
    private Path path;

    private Mode currentMode;

    // * YouTubeDL
    private boolean playList;
    // * M3U8

    public DownloadCombinedRunnable(String[] links) {
        this.links = links;
        path = mainPath.toAbsolutePath();

        if(JFrameManager.INSTANCE.getDownloadM3U8().isSelected()) {
            currentMode = Mode.FFMPEG;
        } else {
            currentMode = Mode.YouTubeDL;
        }
        format = FormatEnum.values()[JFrameManager.INSTANCE.getFormat().getSelectedIndex()];
        playList = JFrameManager.INSTANCE.getDownloadPlaylists().isSelected();
    }

    @Override
    public void run() {
        for(int i = 0; i != links.length; i++) {
            final String s = links[i];
            if(isStopping()) {
                break;
            }

            final String text = s.trim();
            try {
                final String upperText = text.toUpperCase();
                if(upperText.startsWith("FCREATE")) {
                    final String folderName = text.replaceFirst("FCREATE", "").replaceAll("[^a-zA-Z0-9]", " ").trim().replaceAll(" ", "-");//I Don't want to risk having a special character. IK some are supported.
                    path = path.resolve(folderName);
                    if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                        try {
                            Files.createDirectory(path);
                        } catch (IOException e) {
                            Utils.displayMessage("Error", "Failed to create folder?");
                            continue;
                        }
                    }
                    continue;
                } else if(upperText.startsWith("FBACK")) {
                    path = new File(path.toString()+"/..").toPath();
                    continue;
                } else if((upperText.startsWith("COUNTER_RESET") || upperText.startsWith("RESET_COUNTER")) && this.currentMode.equals(Mode.FFMPEG)) {
                    counter = 1;
                    continue;
                } else if(upperText.startsWith("MODE ")) {
                    final String[] args = upperText.split(" ");
                    if (args.length >= 3) {
                        Utils.displayMessage("Invalid Command", "This command is incorrectly written with too many args. %s".formatted(text));
                        return;
                    }

                    switch (args[1]) {
                        case "M3U8", "M3" -> this.currentMode = Mode.FFMPEG;
                        case "YOUTUBE", "YOUTUBEDL", "YT" -> this.currentMode = Mode.YouTubeDL;
                    }
                    continue;
                } else if(upperText.startsWith("FORMAT ") && this.currentMode.equals(Mode.YouTubeDL)) {
                    final String[] args = upperText.split(" ");
                    if (args.length >= 3) {
                        Utils.displayMessage("Invalid Format Command", "This command is incorrectly written with too many args. %s".formatted(text));
                        return;
                    }

                    for(FormatEnum formatEnum : FormatEnum.values()) {
                        if(args[1].equalsIgnoreCase(formatEnum.getDisplay())) {
                            format = formatEnum;
                            break;
                        }
                    }
                    continue;
                } else if(upperText.startsWith("PLAYLIST_ON") && this.currentMode.equals(Mode.YouTubeDL)) {
                    playList = true;
                    continue;
                } else if(upperText.startsWith("PLAYLIST_OFF") && this.currentMode.equals(Mode.YouTubeDL)) {
                    playList = false;
                    continue;
                }


                switch (currentMode) {
                    case YouTubeDL -> {
                        // Build request
                        YoutubeDLRequest request = new YoutubeDLRequest(text);
                        request.setDirectory(path.toString());
                        request.setExtractAudio(format.isAudio());
                        request.setFormat(format.getDisplay());
                        request.setOption("ignore-errors");
                        request.setOption("retries", 10);

                        request.setOption(playList ? "yes-playlist" : "no-playlist");

                        // Make request and return response;
                        final int finalI = i;
                        YoutubeDLResponse response = YoutubeDL.execute(request, (progress, etaInSeconds) -> JFrameManager.INSTANCE.setProgress(progress, etaInSeconds, finalI, links.length));

                        // Response
                        String stdOut = response.out(); // Executable output
                        //System.out.println(stdOut);
                    } case FFMPEG -> {
                        // Build request
                        m3u8Request request = new m3u8Request(text, counter);
                        counter++;
                        request.setDirectory(path.toString());

                        // Make request and return response;
                        final int finalI = i;
                        m3u8Response response = m3u8.execute(request, (progress, etaInSeconds) -> JFrameManager.INSTANCE.setProgress(progress, etaInSeconds, finalI, links.length));

                        // Response
                        String stdOut = response.out(); // Executable output
                        //System.out.println(stdOut);
                    }
                }
            } catch (YoutubeDLException e) {
                if(isStopping()) {
                    Utils.displayMessage("Stopped", "Stopped all video downloads.");
                    return;
                }

                //Print stack anyway.
                e.printStackTrace();

                //We display custom error message with detail on the issue.
                Utils.displayMessage("YT-DL Error",e.getMessage());
                return;
            }
        }

        Utils.displayMessage("Finished", "All video downloads are completed.");
    }

    private enum Mode {
        YouTubeDL,
        FFMPEG
    }
}
