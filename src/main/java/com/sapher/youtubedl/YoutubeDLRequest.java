package com.sapher.youtubedl;

import lombok.Getter;
import lombok.Setter;
import me.third.right.youtubedl.YTDL;
import me.third.right.youtubedl.manager.SettingsManager;

import java.util.ArrayList;
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
    protected String[] buildOptions() {
        final ArrayList<String> commandBuild = new ArrayList<>();
        if (extractAudio) {
            commandBuild.add("-x");
            commandBuild.add("--audio-format");
            commandBuild.add(format.toLowerCase());
        }

        commandBuild.add("-o");
        commandBuild.add("%s/%s".formatted(directory, SettingsManager.INSTANCE.getYtRename().getValue()));

        if(YTDL.isWindows()) {
            commandBuild.add("--no-check-certificate");//Only happens on windows.
        }

        // Build options strings
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry option = (Map.Entry) it.next();

            String name = (String) option.getKey();
            String value = (String) option.getValue();

            if (value == null) value = "";

            commandBuild.add("--%s".formatted(name.trim()));
            if (!value.trim().isEmpty())
                commandBuild.add(value.trim());

            it.remove();
        }

        if (!extractAudio) {
            commandBuild.add("--format");
            commandBuild.add("bestvideo+bestaudio[ext=m4a]/bestvideo+bestaudio/best");
            commandBuild.add("--merge-output-format");
            commandBuild.add(format.toLowerCase());
        }

        // Set Url
        if (url != null)
            commandBuild.add(url);

        commandBuild.removeIf(String::isEmpty);
        return commandBuild.toArray(new String[0]);
    }
}
