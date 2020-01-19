package com.example.cripto_photoaffix.Commands;

import android.content.Intent;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Gallery.Media;
import java.io.File;

public class ImageShareCommand extends ShareCommand {
    public ImageShareCommand() {
        super();
    }

    @Override
    public void execute() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        Media media;

        while (!toExecuteOn.isEmpty()) {
            media = toExecuteOn.poll();

            File file = media.share(activity.getCacheDir().getPath() + "/share/");

            Intent intent = createIntent(file);
            intent.setType("image/jpg");

            activity.startActivity(Intent.createChooser(intent, "Share via:"));
        }
    }
}
