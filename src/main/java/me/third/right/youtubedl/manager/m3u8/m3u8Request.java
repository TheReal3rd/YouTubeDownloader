package me.third.right.youtubedl.manager.m3u8;

import lombok.Getter;
import lombok.Setter;

/**
 * YoutubeDL request
 */
@Getter
@Setter
public class m3u8Request {
    private String directory;
    private String url;

    /**
     * Construct a request with a videoUrl
     * @param url
     */
    public m3u8Request(String url) {
        this.url = url;
    }

    /**
     * Construct a request with a videoUrl and working directory
     * @param url
     * @param directory
     */
    public m3u8Request(String url, String directory) {
        this.url = url;
        this.directory = directory;
    }

    /**
     * Transform options to a string that the executable will execute
     * @return Command string
     */
    protected String buildOptions() {
        final StringBuilder builder = new StringBuilder();

        if (url != null)
            builder.append(url).append(" ");

        return builder.toString().trim();
    }
}
