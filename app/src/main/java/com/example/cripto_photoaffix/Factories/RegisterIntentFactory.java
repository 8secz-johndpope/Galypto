package com.example.cripto_photoaffix.Factories;

import android.content.Context;
import android.content.Intent;

import com.example.cripto_photoaffix.Activities.RegisterActivities.RegisterActivity;

public class RegisterIntentFactory extends IntentFactory {

    public RegisterIntentFactory(Context context) {
        super(context);
    }

    public Intent create() {
        return new Intent(context, RegisterActivity.class);
    }
}
