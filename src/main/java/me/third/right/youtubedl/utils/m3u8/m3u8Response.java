package me.third.right.youtubedl.utils.m3u8;

/**
 * YoutubeDL response
 */
public record m3u8Response(String command, String directory, int exitCode,
                           int elapsedTime, String out, String err) {
}
