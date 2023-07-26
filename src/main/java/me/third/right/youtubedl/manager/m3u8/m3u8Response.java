package me.third.right.youtubedl.manager.m3u8;

/**
 * YoutubeDL response
 */
public record m3u8Response(String command, String directory, int exitCode,
                           int elapsedTime, String out, String err) {
}
