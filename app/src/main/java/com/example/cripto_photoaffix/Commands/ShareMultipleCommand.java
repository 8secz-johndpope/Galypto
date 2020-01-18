package com.example.cripto_photoaffix.Commands;

import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Gallery.Media;
import java.io.File;
import java.util.ArrayList;

public class ShareMultipleCommand extends Command {
    public void execute() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        ArrayList<Uri> list = new ArrayList<Uri>();

        Media media;
        while (!toExecuteOn.isEmpty()) {
            media = toExecuteOn.poll();

            File file = media.share(activity.getCacheDir().getPath() + "/share/");

            Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".fileprovider", file);
            list.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list);

        activity.startActivity(Intent.createChooser(intent, "Share via:"));
    }
}
