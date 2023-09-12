package me.third.right.youtubedl.utils.runnables;

import com.sapher.youtubedl.YoutubeDLException;
import me.third.right.youtubedl.gui.JFrameManager;
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

/**
 * Task used to download a list of M3U8 Videos.
 */
public class DownloadM3U8Runnable extends RunnableBase {
    private final String[] links;
    private int counter = 1;
    private Path path;

    public DownloadM3U8Runnable(String[] links) {
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
                } else if(text.startsWith("COUNTER_RESET")) {
                    counter = 1;
                    continue;
                }

                // Build request
                m3u8Request request = new m3u8Request(text, counter);
                counter++;
                request.setDirectory(path.toString());

                // Make request and return response;
                final int finalI = i;
                m3u8Response response = m3u8.execute(request, (progress, etaInSeconds) -> JFrameManager.INSTANCE.setProgress(progress, etaInSeconds, finalI, links.length));

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
                Utils.displayMessage("YT-DL Error", e.getMessage());
                return;
            }
        }

        Utils.displayMessage("Finished", "All video downloads are completed.");
    }
}
