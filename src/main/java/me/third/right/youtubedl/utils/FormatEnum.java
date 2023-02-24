package me.third.right.youtubedl.utils;

import lombok.Getter;

/**
 * File format definitions.
 */
@Getter
public enum FormatEnum {
    //Video
    MP4("MP4", false),
    MKV("MKV", false),
    //Audio
    M4A("M4A", true),
    MP3("MP3", true);

    private final String display;
    private final boolean audio;
    FormatEnum(String display, boolean audio) {
        this.display = display;
        this.audio = audio;
    }

}
