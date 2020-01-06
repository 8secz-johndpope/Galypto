package com.example.cripto_photoaffix.Commands;

import android.content.Intent;
import android.net.Uri;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Gallery.Media;

import java.io.File;

public class StoreCommand implements Command {
    protected Media media;
    protected MyActivity activity;

    public StoreCommand(MyActivity activity, Media media) {
        this.media = media;
        this.activity = activity;
    }

    @Override
    public void execute() {
        File file = media.store(activity.getExternalMediaDirs()[0].toString());

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        activity.sendBroadcast(intent);
    }
}
