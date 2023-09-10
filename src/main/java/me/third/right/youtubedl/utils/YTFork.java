package me.third.right.youtubedl.utils;

import lombok.Getter;

@Getter
public enum YTFork {
    youtube_dl("youtube-dl", "https://github.com/ytdl-patched/youtube-dl"),
    ytdl_patched("ytdl-patched", "https://github.com/ytdl-patched/ytdl-patched"),
    yt_dlp("yt-dlp","https://github.com/yt-dlp/yt-dlp");

    private String name;//The name of the file to call.
    private String url;//Git location where we download the latest from.
    YTFork(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
