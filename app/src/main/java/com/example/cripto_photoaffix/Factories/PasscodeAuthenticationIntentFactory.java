package com.example.cripto_photoaffix.Factories;

import android.content.Context;
import android.content.Intent;

import com.example.cripto_photoaffix.Activities.LoginActivities.PasscodeAuthenticationActivity;

public class PasscodeAuthenticationIntentFactory extends IntentFactory {

    public PasscodeAuthenticationIntentFactory(Context context) {
        super(context);
    }

    @Override
    public Intent create() {
        return new Intent(context, PasscodeAuthenticationActivity.class);
    }
}
