package me.third.right.youtubedl.utils;

import me.third.right.youtubedl.YTDL;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Manager that checks whether the required tools and programs are installed if not it'll get them.
 */
public class Utils {
    // Paths
    public static final Path mainPath = new File("").toPath();

    /**
     * Check YouTube DL is present if not it'll extract it.
     */
    public static boolean downloaderCheck() {
        if(!Files.exists(mainPath.resolve("youtube-dl"))) {
            extractResource("youtube-dl", mainPath.resolve("youtube-dl"));
            return false;
        }
        return true;
    }

    /**
     * Checks FFMPEG is present if not it'll download it.
     * I'm not planning to add support for Linux.
     */
    public static boolean ffmpegCheck() {
        if(YTDL.isWindows()) {
            //TODO: Need to test this on Windows / Need to know how YouTube-DL looks for ffmpeg. So i can extract the ffmpeg from resource to the correct location.
        }
        return true;
    }


    /**
     * Extracts files form the resource area.
     */
    public static void extractResource(String resourceName, Path destination) {
        final InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(resourceName);
        if(inputStream == null) {
            System.out.println("Failed to find the file.");
            return;
        }

        final File targetFile = destination.toFile();
        try {
            final OutputStream outputStream = new FileOutputStream(targetFile);
            outputStream.write(inputStream.readAllBytes());

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a text file into a String.
     * @param file Location of the Text file.
     * @return The content of the Text file that was read. If the files is not found or an error occurred an empty string will be given.
     */
    public static String loadTextFile(File file) {
        final StringBuilder messages = new StringBuilder();
        final InputStream stream;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }

        final Scanner scanner = new Scanner(stream);
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if(line.isEmpty()) continue;
            messages.append(line).append("\n");
        }

        scanner.close();
        return messages.toString();
    }
}
