package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import com.example.cripto_photoaffix.R;

public class VideoViewerActivity extends ContentViewerActivity {
    protected VideoView videoView;
    protected MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewer);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.videoView);


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
        findViewById(R.id.video_delete_button).setOnTouchListener(mDelayHideTouchListener);

        videoView = findViewById(R.id.videoView);
        String videoPath = media.getPath();

        if (videoPath != null) {
            videoView.setVideoPath(videoPath);
            controller = new MediaController(this);
            videoView.setMediaController(controller);
            controller.setAnchorView(videoView);
        }
    }
}
