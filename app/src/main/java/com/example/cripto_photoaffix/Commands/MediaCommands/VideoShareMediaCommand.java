package com.example.cripto_photoaffix.Commands.MediaCommands;

import android.content.Intent;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Gallery.Media;
import java.io.File;

public class VideoShareMediaCommand extends ShareMediaCommand {

    @Override
    public void execute(Media media) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        File file = media.share(activity.getCacheDir().getPath() + "/share/");

        Intent intent = createIntent(file);
        intent.setType("video/mp4");

        activity.startActivity(Intent.createChooser(intent, "Share via:"));
    }
}
