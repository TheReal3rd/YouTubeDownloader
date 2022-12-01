package me.third.right.youtubedl.runnables;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import me.third.right.youtubedl.manager.JFrameManager;

public class DownloadRunnable implements Runnable {

    boolean stopping = false;

    private final boolean extractAudio;
    private final String format;
    private final String[] links;

    public DownloadRunnable(String[] links, boolean extractAudio, String format) {
        this.extractAudio = extractAudio;
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
                request.setDirectory(System.getProperty("user.dir"));
                request.setExtractAudio(extractAudio);
                request.setFormat(format);
                request.setOption("ignore-errors");
                request.setOption("retries", 10);

                if (JFrameManager.INSTANCE.getDownloadPlaylists().isSelected()) {
                    request.setOption("yes-playlist");
                } else {
                    request.setOption("no-playlist");
                }

                // Make request and return response
                int finalI = i;
                YoutubeDLResponse response = YoutubeDL.execute(request, (progress, etaInSeconds) -> JFrameManager.INSTANCE.setProgress(progress, links.length, finalI));

                // Response
                String stdOut = response.getOut(); // Executable output
                System.out.println(stdOut);
            } catch (YoutubeDLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStopping(boolean stopping) {
        this.stopping = stopping;
    }

    public boolean isStopping() {
        return stopping;
    }
}
