package com.example.cripto_photoaffix.Authenticators;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;

public abstract class Authenticator {
    protected MyActivity activity;

    protected Authenticator(MyActivity c) {
        activity = c;
    }

    public abstract void authenticate();

    public abstract void initialize();

    public abstract boolean canBeUsed();

    public abstract EncryptedFile encrypt(String data);

    public abstract String decrypt(EncryptedFile file);

    public abstract String getFinalPassword();
}
