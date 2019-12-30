package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;

import com.example.cripto_photoaffix.Activities.ImageViewerActivity;
import com.example.cripto_photoaffix.Activities.MyActivity;

public class ImageViewerIntentFactory extends IntentFactory {

    public ImageViewerIntentFactory(MyActivity activity) {
        super(activity);
    }

    public Intent create() {
        return new Intent(activity, ImageViewerActivity.class);
    }
}
