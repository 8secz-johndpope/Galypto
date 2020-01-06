package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.MyActivity;

public class GalleryIntentFactory extends IntentFactory {

    public GalleryIntentFactory(MyActivity activity) {
        super(activity);
    }

    public Intent create() {
        return new Intent(activity, GalleryActivity.class);
    }
}
