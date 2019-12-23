package com.example.cripto_photoaffix.Authenticators;

import com.example.cripto_photoaffix.Activities.MyActivity;

public abstract class Authenticator {
    protected MyActivity activity;

    protected Authenticator(MyActivity c) {
        activity = c;
    }

    public abstract void authenticate();

    public abstract void initialize();

    public abstract boolean canBeUsed();
}
