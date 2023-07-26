package me.third.right.youtubedl.utils.runnables;

import lombok.Getter;
import lombok.Setter;

public class RunnableBase implements Runnable {
    @Setter @Getter
    private boolean stopping = false;

    @Override
    public void run() {

    }
}
