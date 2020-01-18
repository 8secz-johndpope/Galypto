package com.example.cripto_photoaffix.Commands.MediaCommands;

import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Media;

public class DeleteMediaCommand implements MediaCommand {
    public void execute(Media media) {
        FilesManager manager = FilesManager.getInstance();
        System.out.println("Removing: " + media.getFullPath());
        manager.removeFile(media.getFullPath());
        manager.removeFile(media.getPath() + media.getFilename());
    }
}
