package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

import java.util.ArrayList;
import java.util.List;

public class VideoViewerActivity extends ContentViewerActivity {
    protected VideoView videoView;
    protected List<ImageButton> videoControlButtons;

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
    }

    @Override
    public void loadMedia() {
        videoView = (VideoView) mContentView;
        String videoPath = media.getFullPath();

        if (videoPath != null)
            videoView.setVideoPath(videoPath);
    }

    @Override
    public void accept(ActivityVisitor visitor) {}

    @Override
    public void initializeButtons() {
        super.initializeButtons();

        videoControlButtons = new ArrayList<ImageButton>();

        final ImageButton button = findViewById(R.id.play_pause);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    button.setImageResource(R.drawable.play);
                }
                else {
                    videoView.start();
                    button.setVisibility(View.INVISIBLE);
                    button.setImageResource(R.drawable.pause);
                }
            }
        });

        videoControlButtons.add(button);
    }

    @Override
    public void touchScreen() {
        int size = videoControlButtons.size();

        for (int i = 0; i < size; i++) {
            i++;
        }
    }
}
