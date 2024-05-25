package me.third.right.youtubedl.utils.runnables;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RunnableBase implements Runnable {
    @Setter
    private boolean stopping = false;

    @Override
    public void run() {

    }
}
