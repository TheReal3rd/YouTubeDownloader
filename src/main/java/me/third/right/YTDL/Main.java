package me.third.right.YTDL;

import lombok.Getter;

public class Main {

    @Getter
    private static boolean isWindows;

    public static void main(String[] args) {

        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        if(isWindows) {
            //TODO add error message fuck windows. ill add support later.
        }

        CommandManager.INSTANCE = new CommandManager();
        FrameManager.INSTANCE = new FrameManager();
    }


}
