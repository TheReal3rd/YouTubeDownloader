package com.sapher.youtubedl;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * YoutubeDL request
 */
public class YoutubeDLRequest {

    /**
     * Executable working directory
     */
    @Getter @Setter
    private String directory;

    /**
     * Video Url
     */
    @Getter @Setter
    private String url;

    /**
     * List of executable options
     */
    @Getter
    private final Map<String, String> options = new HashMap<String, String>();

    @Getter @Setter
    private boolean extractAudio = false;

    @Getter @Setter
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
     * Construct a request with a videoUrl and working directory
     * @param url
     * @param directory
     */
    public YoutubeDLRequest(String url, String directory) {
        this.url = url;
        this.directory = directory;
    }

    /**
     * Transform options to a string that the executable will execute
     * @return Command string
     */
    protected String buildOptions() {
        final StringBuilder builder = new StringBuilder();

        if(extractAudio) {
            builder.append("-x --audio-format %s ".formatted(format.toLowerCase()));
        }

        // Build options strings
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry option = (Map.Entry) it.next();

            String name = (String) option.getKey();
            String value = (String) option.getValue();

            if(value == null) value = "";

            String optionFormatted = String.format("--%s %s", name, value).trim();
            builder.append(optionFormatted + " ");

            it.remove();
        }

        if(!extractAudio) {
            builder.append("--format bestvideo+bestaudio[ext=m4a]/bestvideo+bestaudio/best --merge-output-format %s ".formatted(format.toLowerCase()));
        }

        // Set Url
        if(url != null)
            builder.append(url + " ");

        System.out.println(builder);

        return builder.toString().trim();
    }
}
