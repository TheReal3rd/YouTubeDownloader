package me.third.right.youtubedl.utils.runnables;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.utils.FormatEnum;
import me.third.right.youtubedl.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static me.third.right.youtubedl.utils.Utils.mainPath;

/**
 * Task used to download a list of YouTube Videos.
 */
public class DownloadYTRunnable extends RunnableBase {
    private final FormatEnum format;
    private final String[] links;
    private Path path;

    public DownloadYTRunnable(String[] links, FormatEnum format) {
        this.format = format;
        this.links = links;
        path = mainPath.toAbsolutePath();
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
                if(text.startsWith("FCREATE")) {
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
                } else if(text.startsWith("FBACK")) {
                    path = new File(path.toString()+"/..").toPath();
                    continue;
                }


                // Build request
                YoutubeDLRequest request = new YoutubeDLRequest(text);
                request.setDirectory(path.toString());
                request.setExtractAudio(format.isAudio());
                request.setFormat(format.getDisplay());
                request.setOption("ignore-errors");
                request.setOption("retries", 10);

                request.setOption(JFrameManager.INSTANCE.getDownloadPlaylists().isSelected() ? "yes-playlist" : "no-playlist");

                // Make request and return response;
                final int finalI = i;
                YoutubeDLResponse response = YoutubeDL.execute(request, (progress, etaInSeconds) -> JFrameManager.INSTANCE.setProgress(progress, etaInSeconds, finalI, links.length));

                // Response
                String stdOut = response.out(); // Executable output
                System.out.println(stdOut);
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
}
