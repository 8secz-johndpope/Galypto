package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.cripto_photoaffix.R;

public class ImageViewerActivity extends ContentViewerActivity {
    protected ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.imageView);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.delete_button).setOnTouchListener(mDelayHideTouchListener);

        imageView = (ImageView) mContentView;
        imageView.setImageBitmap(media.getPreview());

        initializeButtons();
    }
}
