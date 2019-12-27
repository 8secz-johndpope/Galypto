package com.example.cripto_photoaffix.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.Visitor;

public abstract class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void accept(Visitor visitor);
}
