package com.example.cripto_photoaffix.Activities;

import android.os.Bundle;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.Visitor;

public class GalleryActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_activity);
    }

    public void accept(Visitor visitor) {}
}
