package me.third.right.YTDL;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import lombok.Getter;
import lombok.Setter;

public class DownloadRunnable implements Runnable {

    @Getter @Setter
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
        for(String s : links) {
            if(stopping) {
                break;
            }

            final String text = s.trim();
            try {
                // Build request
                YoutubeDLRequest request = new YoutubeDLRequest(text);
                request.setExtractAudio(extractAudio);
                request.setFormat(format);
                request.setOption("ignore-errors");
                request.setOption("retries", 10);

                if (FrameManager.INSTANCE.getDownloadPlaylists().isSelected()) {
                    request.setOption("yes-playlist");
                } else {
                    request.setOption("no-playlist");
                }

                // Make request and return response
                YoutubeDLResponse response = YoutubeDL.execute(request, (progress, etaInSeconds) -> FrameManager.INSTANCE.setProgress(progress));

                // Response
                String stdOut = response.getOut(); // Executable output
                System.out.println(stdOut);
            } catch (YoutubeDLException e) {
                e.printStackTrace();
            }
        }
    }

}
