package me.third.right.youtubedl.utils.m3u8;

import lombok.Getter;
import lombok.Setter;

/**
 * m3u8 request
 */
@Getter
@Setter
public class m3u8Request {
    private String directory;
    private String url;

    private int id;

    /**
     * Construct a request with a videoUrl
     * @param url m3u8 url.
     * @param id the ID / EP ID : EP 1
     */
    public m3u8Request(String url, int id) {
        this.url = url;
        this.id = id;
    }

    /**
     * Transform options to a string that the executable will execute
     * @return Command string
     */
    protected String buildOptions() {
        final StringBuilder builder = new StringBuilder();

        if (url != null)
            builder.append(url).append(" ");

        builder.append("-o EP %d".formatted(this.id));//TODO add an option / method to split this to seasons and different name.

        return builder.toString().trim();
    }
}
