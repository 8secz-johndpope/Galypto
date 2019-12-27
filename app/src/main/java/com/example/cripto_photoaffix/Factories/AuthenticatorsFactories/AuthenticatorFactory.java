package com.example.cripto_photoaffix.Factories.AuthenticatorsFactories;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;

public abstract class AuthenticatorFactory {
    protected MyActivity activity;

    protected AuthenticatorFactory(MyActivity activity) {
        this.activity = activity;
    }

    public abstract Authenticator create();
}
