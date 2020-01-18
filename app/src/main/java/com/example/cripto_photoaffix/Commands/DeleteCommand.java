package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Media;

public class DeleteCommand implements Command {
    private Media media;

    public DeleteCommand(Media media) {
        this.media = media;
    }

    public void execute() {
        FilesManager manager = FilesManager.getInstance();
        System.out.println("Removing: " + media.getFullPath());
        manager.removeFile(media.getFullPath());
        manager.removeFile(media.getPath() + media.getFilename());
    }
}
