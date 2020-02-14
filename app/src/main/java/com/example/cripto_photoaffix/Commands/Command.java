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
     * Ejecuta la tarea.
     */
    public abstract void execute();

    /**
     * Añade elementos en los cuales ejecutar la tarea.
     * @param media Media en la cual ejecutar la tarea.
     */
    public void addMedia(Media media){
        toExecuteOn.add(media);
    }
}
