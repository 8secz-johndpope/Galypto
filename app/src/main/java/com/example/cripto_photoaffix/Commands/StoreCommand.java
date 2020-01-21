package com.example.cripto_photoaffix.Commands;

import android.content.Intent;
import android.net.Uri;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Gallery.Media;
import java.io.File;

public class StoreCommand extends Command {
    @Override
    public void execute() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        Media media;

        while (!toExecuteOn.isEmpty()) {
            media = toExecuteOn.poll();

            File file = media.store(activity.getExternalMediaDirs()[0].toString());

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            activity.sendBroadcast(intent);
        }
    }
}
