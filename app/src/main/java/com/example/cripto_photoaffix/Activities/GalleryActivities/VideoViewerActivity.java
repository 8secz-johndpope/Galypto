package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.media.MediaPlayer;
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
        findViewById(R.id.delete_button).setOnTouchListener(mDelayHideTouchListener);

        videoView = (VideoView) mContentView;
        String videoPath = media.getFullPath();

        if (videoPath != null) {
            videoView.setVideoPath(videoPath);
            controller = new MediaController(this);
            videoView.setMediaController(controller);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    controller.show();
                }
            });
        }

        initializeButtons();
    }
}
