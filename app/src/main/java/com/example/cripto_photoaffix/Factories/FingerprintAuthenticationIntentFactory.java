package com.example.cripto_photoaffix.Factories;

import android.content.Context;
import android.content.Intent;

import com.example.cripto_photoaffix.Activities.LoginActivities.FingerprintAuthenticationActivity;
import com.example.cripto_photoaffix.Authenticators.FingerprintAuthenticator;

public class FingerprintAuthenticationIntentFactory extends IntentFactory {

    public FingerprintAuthenticationIntentFactory(Context context) {
        super(context);
    }

    public Intent create() {
        return new Intent(context, FingerprintAuthenticationActivity.class);
    }
}
