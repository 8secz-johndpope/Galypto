package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Media;

public class DeleteCommand extends Command {
    public DeleteCommand() {
        super();
    }

    public void execute() {
        Media media;

        while (!toExecuteOn.isEmpty()) {
            media = toExecuteOn.poll();

            FilesManager manager = FilesManager.getInstance();
            System.out.println("Removing: " + media.getFullPath());
            manager.removeFile(media.getFullPath());
            manager.removeFile(media.getPath() + media.getFilename());
        }
    }
}
