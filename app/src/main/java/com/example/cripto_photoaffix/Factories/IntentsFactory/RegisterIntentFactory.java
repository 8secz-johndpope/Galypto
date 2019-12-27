package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Activities.RegisterActivities.RegisterActivity;

public class RegisterIntentFactory extends IntentFactory {

    public RegisterIntentFactory(MyActivity activity) {
        super(activity);
    }

    public Intent create() {
        return new Intent(activity, RegisterActivity.class);
    }
}
