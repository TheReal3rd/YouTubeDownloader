package com.sapher.youtubedl;

import java.util.Map;

/**
 * YoutubeDL response
 */
public record YoutubeDLResponse(String command, Map<String, String> options, String directory, int exitCode,
                                int elapsedTime, String out, String err) {
}
