package com.example.cripto_photoaffix.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import com.example.cripto_photoaffix.R;

public class ImageViewerActivity extends ContentViewerActivity {
    protected ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_viewer);

        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(media.getPreview());
    }
}
