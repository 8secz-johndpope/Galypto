package com.example.cripto_photoaffix.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

public abstract class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTransferer activityTransferer = ActivityTransferer.getInstance();
        activityTransferer.setActivity(this);
    }

    public abstract void accept(ActivityVisitor activityVisitor);

    public abstract void refresh();
}
