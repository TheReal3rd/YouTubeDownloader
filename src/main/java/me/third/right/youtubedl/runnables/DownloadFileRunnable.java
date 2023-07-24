package me.third.right.youtubedl.runnables;

import me.third.right.youtubedl.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DownloadFileRunnable implements Runnable {
    private final Path destination;
    private final URL source;
    private final boolean overwrite;

    public DownloadFileRunnable(Path destination, URL source, boolean overwrite) {
        this.destination = destination;
        this.source = source;
        this.overwrite = overwrite;
    }

    @Override
    public void run() {
        if(Files.exists(destination) && !overwrite) {
            Utils.displayError("The File already exists.");
            return;
        }

        try(InputStream in = source.openStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            Utils.displayError("Finished downloading from "+source.toString());
        } catch (IOException e) {
            Utils.displayError("Failed to download the required files!");
            e.printStackTrace();
        }
    }
}