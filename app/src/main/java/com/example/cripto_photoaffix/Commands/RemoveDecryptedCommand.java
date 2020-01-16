package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import java.util.List;

public class RemoveDecryptedCommand implements Command {
    private FilesManager manager;

    public RemoveDecryptedCommand(MyActivity activity) {
        manager = FilesManager.getInstance();
    }

    @Override
    public void execute() {
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
