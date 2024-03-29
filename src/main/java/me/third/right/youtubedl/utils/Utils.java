package me.third.right.youtubedl.utils;

import me.third.right.youtubedl.YTDL;
import me.third.right.youtubedl.gui.ErrorFrame;
import me.third.right.youtubedl.gui.JFrameManager;
import me.third.right.youtubedl.manager.SettingsManager;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
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
        return Files.exists(mainPath.resolve(SettingsManager.INSTANCE.getSourceSetting().getSelected().getName()));
    }

    /**
     * Check FFMPEG is present. Linux one doesn't work just tell the user to install it.
     */
    public static boolean ffmpegCheck() {
        if(!YTDL.isWindows()) return false;
        String name = "winFFmpeg/ffmpeg.exe";
        return Files.exists(mainPath.resolve(name));
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

    public static boolean deleteFile(Path path) {
        try {
            return Files.deleteIfExists(path);
        } catch (IOException err) {
            System.out.printf("Failed to do delete operation. %s", err);
            return false;
        }
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
        System.out.println("\n\n%s\n%s".formatted(title, message));
    }

    /**
     * Gets the latest version of a github project.
     * @return the version number / name. (Null will be returned if it fails.)
     */
    public static String getGitLatest(URL gitUrl) {
        try {
            final HttpsURLConnection con = (HttpsURLConnection) gitUrl.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setInstanceFollowRedirects(true);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return con.getURL().toString().replaceAll( gitUrl.toString().replaceAll("/latest/", "/tag/"),"");
            } else return null;
        } catch (IOException err) {
            Utils.displayMessage("Git Error", "Something went wrong. { %s }".formatted(err));
            System.out.println(err);
            return null;
        }
    }

    /**
     * Read the name.
     * @param destination read the name.
     * @param source read the name.
     * @param overwrite Whether to overwrite or not.
     */
    public static void downloadFile(Path destination, URL source, boolean overwrite) {
        if(Files.exists(destination) && !overwrite) {
            Utils.displayMessage("Download Error","The File already exists.");
            return;
        }

        try(InputStream in = source.openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            Utils.displayMessage("YT-DL download complete!","Finished downloading from "+ source);
        } catch (IOException e) {
            Utils.displayMessage("Error","Failed to download the required files!");
            e.printStackTrace();
        }
    }
}
