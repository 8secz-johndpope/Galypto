package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;

import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Activities.MyActivity;

public class LoginIntentFactory extends IntentFactory {

    public LoginIntentFactory(MyActivity activity) {
        super(activity);
    }

    public Intent create() {
        return new Intent(activity, LoginActivity.class);
    }
}
