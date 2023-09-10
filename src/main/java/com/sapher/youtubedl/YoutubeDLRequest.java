package com.sapher.youtubedl;

import lombok.Getter;
import lombok.Setter;
import me.third.right.youtubedl.YTDL;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * YoutubeDL request
 */
@Getter
@Setter
public class YoutubeDLRequest {


    private String directory;

    private String url;

    private final Map<String, String> options = new HashMap<>();

    private boolean extractAudio = false;

    private String format = null;

    public void setOption(String key) {
        options.put(key, null);
    }

    public void setOption(String key, String value) {
        options.put(key, value);
    }

    public void setOption(String key, int value) {
        options.put(key, String.valueOf(value));
    }

    /**
     * Constructor
     */
    public YoutubeDLRequest() {

    }

    /**
     * Construct a request with a videoUrl
     * @param url
     */
    public YoutubeDLRequest(String url) {
        this.url = url;
    }

    /**
     * Transform options to a string that the executable will execute
     * @return Command string
     */
    protected String buildOptions() {
        final StringBuilder builder = new StringBuilder();
        if (extractAudio) {
            builder.append("-x --audio-format %s ".formatted(format.toLowerCase()));
        }

        if(YTDL.isWindows()) {
            builder.append("--no-check-certificate ");//Only happens on windows.
        }

        // Build options strings
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry option = (Map.Entry) it.next();

            String name = (String) option.getKey();
            String value = (String) option.getValue();

            if (value == null) value = "";

            String optionFormatted = String.format("--%s %s", name, value).trim();
            builder.append(optionFormatted).append(" ");

            it.remove();
        }

        if (!extractAudio) {
            builder.append("--format bestvideo+bestaudio[ext=m4a]/bestvideo+bestaudio/best --merge-output-format %s ".formatted(format.toLowerCase()));
        }

        // Set Url
        if (url != null)
            builder.append(url).append(" ");

        return builder.toString().trim();
    }
}
