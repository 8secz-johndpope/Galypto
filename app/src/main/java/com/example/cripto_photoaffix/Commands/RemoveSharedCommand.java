package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Media;
import java.util.List;

public class RemoveSharedCommand extends Command {
    @Override
    public void execute() {
        FilesManager manager = FilesManager.getInstance();
        List<String> media = manager.getShared();
        int size = media.size();

        for (int i = 0; i < size; i++)
            manager.removeFile(media.get(i));
    }

    public void addMedia(Media media) {}
}
