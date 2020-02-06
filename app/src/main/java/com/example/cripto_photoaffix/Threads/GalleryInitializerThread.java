package com.example.cripto_photoaffix.Threads;

import android.net.Uri;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.GalleryTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Gallery.Gallery;
import java.util.List;

public class GalleryInitializerThread extends Thread {
    private List<Uri> toEncrypt;
    private String password;

    public GalleryInitializerThread(List<Uri> toEncrypt, String password) {
        this.toEncrypt = toEncrypt;
        this.password = password;
    }

    public void run() {
        MyActivity currentActivity = ActivityTransferer.getInstance().getActivity();

        Gallery gallery;

        if (toEncrypt.isEmpty())
            gallery = new Gallery(password);
        else
            gallery = new Gallery(password, toEncrypt);

        GalleryTransferer transferer = GalleryTransferer.getInstance();
        transferer.setGallery(gallery);

        IntentFactory factory = new GalleryIntentFactory();
        currentActivity.startActivity(factory.create());

        currentActivity.finish();
    }
}
