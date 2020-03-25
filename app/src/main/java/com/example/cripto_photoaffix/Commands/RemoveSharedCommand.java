package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Media;
import java.util.List;

public class RemoveSharedCommand extends Command {
    @Override
    public void execute() {
        System.out.println("Executing shared removal.");
        FilesManager manager = FilesManager.getInstance();
        List<String> media = manager.getShared();
        int size = media.size();

        for (int i = 0; i < size; i++) {
            System.out.println("Removing: " + media.get(i));
            manager.removeFile(media.get(i));
        }
    }
}
