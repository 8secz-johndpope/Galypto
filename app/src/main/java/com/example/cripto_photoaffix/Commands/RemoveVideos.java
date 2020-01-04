package com.example.cripto_photoaffix.Commands;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import java.util.List;

public class RemoveVideos implements Command {
    private FilesManager manager;

    public RemoveVideos(MyActivity activity) {
        manager = new FilesManager(activity);
    }

    @Override
    public void execute() {
        List<String> media = manager.getMedia();

        for (String file: media) {
            System.out.println(file);
            if (file.endsWith(".mp4")) {
                manager.removeFile(file);
                System.out.println(file + " removed!");
            }
        }
    }
}
