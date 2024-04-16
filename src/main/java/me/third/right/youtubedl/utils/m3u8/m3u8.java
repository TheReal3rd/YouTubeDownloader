package me.third.right.youtubedl.utils.m3u8;

import com.sapher.youtubedl.DownloadProgressCallback;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.utils.StreamGobbler;
import com.sapher.youtubedl.utils.StreamProcessExtractor;
import me.third.right.youtubedl.manager.DownloadManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sapher.youtubedl.YoutubeDL.getExecutablePath;
import static me.third.right.youtubedl.utils.Utils.mainPath;

/**
 * Used to start m3u8 downloads with youtube-dl
 */
public class m3u8 {

    protected static String[] buildCommand(String[] command) {
        final ArrayList<String> commandBuild = new ArrayList<>();
        commandBuild.addAll(List.of(getExecutablePath()));
        commandBuild.addAll(List.of(command));
        return commandBuild.toArray(new String[0]);
    }

    /**
     * Execute youtube-dl request
     * @param request request object
     * @param callback callback
     * @return response object
     * @throws YoutubeDLException
     */
    public static m3u8Response execute(m3u8Request request, DownloadProgressCallback callback) throws YoutubeDLException {
        String[] command = buildCommand(request.buildOptions());
        String directory = mainPath.toAbsolutePath().toString();

        System.out.println(Arrays.toString(command));

        Process process;
        int exitCode;
        StringBuffer outBuffer = new StringBuffer(); //stdout
        StringBuffer errBuffer = new StringBuffer(); //stderr
        long startTime = System.nanoTime();

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        // Define directory if one is passed
        if(directory != null)
            processBuilder.directory(new File(directory));

        try {
            process = processBuilder.start();
            DownloadManager.INSTANCE.setProcess(process);
        } catch (IOException e) {
            throw new YoutubeDLException(e);
        }

        InputStream outStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();

        StreamProcessExtractor stdOutProcessor = new StreamProcessExtractor(outBuffer, outStream, callback);
        StreamGobbler stdErrProcessor = new StreamGobbler(errBuffer, errStream);

        try {
            stdOutProcessor.join();
            stdErrProcessor.join();
            exitCode = process.waitFor();
        } catch (InterruptedException e) {

            // process exited for some reason
            throw new YoutubeDLException(e);
        }

        String out = outBuffer.toString();
        String err = errBuffer.toString();

        if(exitCode > 0) {
            throw new YoutubeDLException(err);
        }

        int elapsedTime = (int) ((System.nanoTime() - startTime) / 1000000);

        final StringBuilder stringBuilder = new StringBuilder();
        for(String string : command) {
            stringBuilder.append(string);
        }

        return new m3u8Response(stringBuilder.toString(), directory, exitCode , elapsedTime, out, err);
    }
}
