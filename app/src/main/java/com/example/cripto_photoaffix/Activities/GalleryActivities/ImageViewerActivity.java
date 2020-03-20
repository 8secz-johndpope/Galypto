package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.cripto_photoaffix.R;

public class ImageViewerActivity extends ContentViewerActivity {
    protected ImageView imageView;
    private int rotated;

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

        rotated = 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        imageView.setImageBitmap(null);
    }

    @Override
    public void loadMedia() {
        imageView = (ImageView) mContentView;
        imageView.setImageBitmap(BitmapFactory.decodeFile(media.getFullPath()));
    }

    @Override
    protected void initializeButtons() {
        super.initializeButtons();

        ImageButton button = findViewById(R.id.rotate_right_button);
        button.setBackgroundColor(Color.TRANSPARENT);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotated += 1;

                rotate();
            }
        });

        button = findViewById(R.id.rotate_left_button);
        button.setBackgroundColor(Color.TRANSPARENT);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotated -= 1;

                rotate();
            }
        });
    }

    private void rotate() {
        imageView.setRotation(90f*rotated);
    }
}
