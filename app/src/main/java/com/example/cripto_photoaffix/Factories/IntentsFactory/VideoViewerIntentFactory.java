package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.VideoViewerActivity;

public class VideoViewerIntentFactory extends IntentFactory {
    public VideoViewerIntentFactory(MyActivity activity) {
        super(activity);
    }

    public Intent create() {
        return new Intent(activity, VideoViewerActivity.class);
    }
}
