package com.example.cripto_photoaffix;

import com.example.cripto_photoaffix.Activities.MyActivity;

public class ActivityTransferer {
    private static final ActivityTransferer instance = new ActivityTransferer();
    private MyActivity activity;

    private ActivityTransferer() {
        activity = null;
    }

    public static ActivityTransferer getInstance() {
        return instance;
    }

    public MyActivity getActivity() {
        return activity;
    }

    public void setActivity(MyActivity d) {
        activity = d;
    }
}
