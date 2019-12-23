package com.example.cripto_photoaffix.Factories;

import android.content.Intent;

import com.example.cripto_photoaffix.Activities.MyActivity;

public abstract class IntentFactory {
    protected MyActivity activity;

    protected IntentFactory(MyActivity activity) {
        this.activity = activity;
    }

    public abstract Intent create();

    public MyActivity getActivity() {
        return activity;
    }
}
