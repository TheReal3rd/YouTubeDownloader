package me.third.right.youtubedl.utils;

import me.third.right.youtubedl.YTDL;
import me.third.right.youtubedl.manager.ErrorFrame;
import me.third.right.youtubedl.manager.JFrameManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * Manager that checks whether the required tools and programs are installed if not it'll get them.
 */
public class Utils {
    // Paths
    public static final Path mainPath;

    static {
        try {
            mainPath = Path.of(new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check YouTube DL is present.
     */
    public static boolean downloaderCheck() {
        if(!Files.exists(mainPath.resolve("youtube-dl"))) {
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
        } else {

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

    /**
     * Displays an error frame used to tell the user what the program is doing or what needs to be fixed.
     * @param message The error message that will be displayed to the user.
     */
    public static void displayMessage(String title, String message) {
        ErrorFrame frame = JFrameManager.INSTANCE.getErrorFrame();
        frame.setTitle(title);
        frame.setMessage(message);
        frame.setVisible(true);
    }

    /**
     * Gets the latest version of a github project.
     * @return the version number / name. (Null will be returned if it fails.)
     */
    public static String getGitLatest(URL gitUrl) {
        try {
            final HttpURLConnection con = (HttpURLConnection) gitUrl.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return con.getURL().toString().replaceAll( gitUrl.toString().replaceAll("/latest/", "/tag/"),"");
            } else return null;
        } catch (IOException err) {
            Utils.displayMessage("Git Error", "Something went wrong. { %s }".formatted(err));
            return null;
        }
    }

    public static void downloadFile(Path destination, URL source, boolean overwrite) {
        if(Files.exists(destination) && !overwrite) {
            Utils.displayMessage("Download Error","The File already exists.");
            return;
        }

        try(InputStream in = source.openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            Utils.displayMessage("YT-DL download complete!","Finished downloading from "+source.toString());
        } catch (IOException e) {
            Utils.displayMessage("Error","Failed to download the required files!");
            e.printStackTrace();
        }
    }
}
