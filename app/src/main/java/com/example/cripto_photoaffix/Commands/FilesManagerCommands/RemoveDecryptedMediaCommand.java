package com.example.cripto_photoaffix.Commands.FilesManagerCommands;

import com.example.cripto_photoaffix.FileManagement.FilesManager;
import java.util.List;

public class RemoveDecryptedMediaCommand implements FilesManagerCommand {

    @Override
    public void execute() {
        FilesManager manager = FilesManager.getInstance();
        List<String> media = manager.getMedia();
        System.out.println("Executing removal.");

        for (String file: media) {
            if (file.endsWith(".mp4") || file.endsWith(".jpg")) {
                System.out.println("Removing: " + file);
                manager.removeFile(file);
            }
        }
    }
}
