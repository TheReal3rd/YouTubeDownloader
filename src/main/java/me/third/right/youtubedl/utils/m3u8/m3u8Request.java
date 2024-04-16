package me.third.right.youtubedl.utils.m3u8;

import lombok.Getter;
import lombok.Setter;
import me.third.right.youtubedl.manager.SettingsManager;

import java.util.ArrayList;

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
    protected String[] buildOptions() {
        final ArrayList<String> commandBuild = new ArrayList<>();

        if (url != null)
            commandBuild.add(url);

        final String rename = SettingsManager.INSTANCE.getM3Rename().getValue().formatted(this.id);//Spaces don't work lol. TODO fix this.
        commandBuild.add("-o");
        commandBuild.add("%s/%s.mp4".formatted(directory, rename));
        return commandBuild.toArray(new String[0]);
    }
}
