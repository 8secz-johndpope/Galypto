package com.example.cripto_photoaffix.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.ActivityVisitor;

public abstract class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void accept(ActivityVisitor activityVisitor);
}
