package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.Gallery.Media;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public abstract class Command {
    protected Queue<Media> toExecuteOn;

    protected Command() {
        toExecuteOn = new LinkedTransferQueue<>();
    }

    /**
     * Execute task.
     */
    public abstract void execute();

    /**
     * Adds elements to which execute the task in.
     * @param media Media to which execute the task.
     */
    public void addMedia(Media media){
        toExecuteOn.add(media);
    }
}
