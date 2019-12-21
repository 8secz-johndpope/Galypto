package com.example.cripto_photoaffix.Factories;

import android.content.Context;
import android.content.Intent;

import com.example.cripto_photoaffix.Activities.GalleryAtivity;

public class GalleryIntentFactory extends IntentFactory {

    public GalleryIntentFactory(Context context) {
        super(context);
    }

    public Intent create() {
        return new Intent(context, GalleryAtivity.class);
    }
}
