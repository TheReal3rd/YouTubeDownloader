package me.third.right.youtubedl.runnables;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import lombok.Getter;
import lombok.Setter;
import me.third.right.youtubedl.manager.ErrorFrame;
import me.third.right.youtubedl.manager.JFrameManager;
import me.third.right.youtubedl.utils.FormatEnum;

import static me.third.right.youtubedl.utils.Utils.mainPath;

/**
 * Task used to download a list of YouTube Videos.
 */
public class DownloaderRunnable implements Runnable {

    @Setter @Getter
    private boolean stopping = false;

    private final FormatEnum format;
    private final String[] links;

    public DownloaderRunnable(String[] links, FormatEnum format) {
        this.format = format;
        this.links = links;
    }

    @Override
    public void run() {
        for(int i = 0; i != links.length; i++) {
            final String s = links[i];
            if(stopping) {
                break;
            }

            final String text = s.trim();
            try {
                // Build request
                YoutubeDLRequest request = new YoutubeDLRequest(text);
                request.setDirectory(mainPath.toAbsolutePath().toString());
                request.setExtractAudio(format.isAudio());
                request.setFormat(format.getDisplay());
                request.setOption("ignore-errors");
                request.setOption("retries", 10);

                request.setOption(JFrameManager.INSTANCE.getDownloadPlaylists().isSelected() ? "yes-playlist" : "no-playlist");

                // Make request and return response;
                final int finalI = i;
                YoutubeDLResponse response = YoutubeDL.execute(request, (progress, etaInSeconds) -> {
                    JFrameManager.INSTANCE.setProgress(progress, etaInSeconds, finalI, links.length);
                });

                // Response
                String stdOut = response.getOut(); // Executable output
                System.out.println(stdOut);
            } catch (YoutubeDLException e) {
                //Print stack anyway.
                e.printStackTrace();

                //We display custom error message with detail on the issue.
                ErrorFrame frame = JFrameManager.INSTANCE.getErrorFrame();
                frame.setError(e.getMessage());
                frame.setVisible(true);
            }
        }
    }
}
