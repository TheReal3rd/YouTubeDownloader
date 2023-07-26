package me.third.right.youtubedl.utils.runnables;

import com.sapher.youtubedl.YoutubeDLException;
import me.third.right.youtubedl.manager.m3u8.m3u8;
import me.third.right.youtubedl.manager.m3u8.m3u8Request;
import me.third.right.youtubedl.manager.m3u8.m3u8Response;
import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.utils.Utils;

import static me.third.right.youtubedl.utils.Utils.mainPath;

/**
 * Task used to download a list of YouTube Videos.
 */
public class DownloadM3U8Runnable extends RunnableBase {
    private final String[] links;

    public DownloadM3U8Runnable(String[] links) {
        this.links = links;
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
                // Build request
                m3u8Request request = new m3u8Request(text);
                request.setDirectory(mainPath.toAbsolutePath().toString());

                // Make request and return response;
                final int finalI = i;
                m3u8Response response = m3u8.execute(request, (progress, etaInSeconds) -> {
                    JFrameManager.INSTANCE.setProgress(progress, etaInSeconds, finalI, links.length);
                });

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
