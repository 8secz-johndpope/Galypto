package com.example.cripto_photoaffix.Commands;

import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.ShareIntentFactory;
import com.example.cripto_photoaffix.Gallery.Media;
import java.io.File;
import java.util.ArrayList;

public class ShareCommand extends Command {

    @Override
    public void execute() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        IntentFactory factory = new ShareIntentFactory();
        Intent intent = factory.create();

        ArrayList<Uri> list = new ArrayList<Uri>();

        Media media;

        while (!toExecuteOn.isEmpty()) {
            media = toExecuteOn.poll();

            File file = media.store(activity.getCacheDir().getPath() + "/share/");

            Uri uri = FileProvider.getUriForFile(activity,
                    "com.example.cripto_photoaffix.fileprovider", file);    //Esta string "authority" debe estar "hardcodeada"
                                                                                     //Ya que es única para cada App, es como una ID.

            list.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list);

        activity.startActivity(Intent.createChooser(intent, "Share via:"));
    }
}
