package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.Gallery.Media;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public abstract class Command {
    protected Queue<Media> toExecuteOn;

    public abstract void execute();

    public void addMedia(Media media){
        if (toExecuteOn == null)
            toExecuteOn = new LinkedTransferQueue<Media>();

        toExecuteOn.add(media);
    }
}
