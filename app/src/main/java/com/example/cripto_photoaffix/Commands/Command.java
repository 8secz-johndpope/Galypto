package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.Gallery.Media;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public abstract class Command {
    protected Queue<Media> toExecuteOn;

    protected Command() {
        toExecuteOn = new LinkedTransferQueue<>();
    }

    public abstract void execute();

    public void addMedia(Media media){
        toExecuteOn.add(media);
    }
}
