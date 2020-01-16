package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;
import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;

public class LoginIntentFactory extends IntentFactory {
    public Intent create() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        return new Intent(activity, LoginActivity.class);
    }
}
