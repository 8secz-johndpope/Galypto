package com.example.cripto_photoaffix.Factories;

import android.content.Context;
import android.content.Intent;

public abstract class IntentFactory {
    protected Context context;

    protected IntentFactory(Context context) {
        this.context = context;
    }

    public abstract Intent create();

    public Context getContext() {
        return context;
    }
}
