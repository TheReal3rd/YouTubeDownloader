package me.third.right.youtubedl;

import me.third.right.youtubedl.manager.SettingsManager;

public class shutHook extends Thread {
    @Override
    public void run() {
        SettingsManager.INSTANCE.saveSettings();
    }
}
