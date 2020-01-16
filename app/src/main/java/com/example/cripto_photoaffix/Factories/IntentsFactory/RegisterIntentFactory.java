package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Activities.RegisterActivities.RegisterActivity;
import com.example.cripto_photoaffix.ActivityTransferer;

public class RegisterIntentFactory extends IntentFactory {

    public Intent create() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        return new Intent(activity, RegisterActivity.class);
    }
}
