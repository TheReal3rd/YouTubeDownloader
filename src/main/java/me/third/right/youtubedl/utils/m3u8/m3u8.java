package me.third.right.youtubedl.utils.m3u8;

import com.sapher.youtubedl.DownloadProgressCallback;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.utils.StreamGobbler;
import com.sapher.youtubedl.utils.StreamProcessExtractor;
import me.third.right.youtubedl.manager.DownloadManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Used to start m3u8 downloads with youtube-dl
 */
public class m3u8 {
    protected static String executablePath = "python3 youtube-dl";

    protected static String buildCommand(String command) {
        return String.format("%s %s", executablePath, command);
    }

    public static m3u8Response execute(m3u8Request request) throws YoutubeDLException {
        return execute(request, null);
    }

    /**
     * Execute youtube-dl request
     * @param request request object
     * @param callback callback
     * @return response object
     * @throws YoutubeDLException
     */
    public static m3u8Response execute(m3u8Request request, DownloadProgressCallback callback) throws YoutubeDLException {
        String command = buildCommand(request.buildOptions());
        String directory = request.getDirectory();

        System.out.println(command);

        Process process;
        int exitCode;
        StringBuffer outBuffer = new StringBuffer(); //stdout
        StringBuffer errBuffer = new StringBuffer(); //stderr
        long startTime = System.nanoTime();

        String[] split = command.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(split);

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

        return new m3u8Response(command, directory, exitCode , elapsedTime, out, err);
    }
}
