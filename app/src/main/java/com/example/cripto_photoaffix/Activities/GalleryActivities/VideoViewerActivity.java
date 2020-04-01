package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.VideoView;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ViewerStates.PlayingVideoState;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ViewerStates.State;
import com.example.cripto_photoaffix.Factories.ButtonFactories.ButtonFactory;
import com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons.VideoButtons.PlayPauseButton;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

import java.util.ArrayList;
import java.util.List;

public class VideoViewerActivity extends ContentViewerActivity {
    protected VideoView videoView;
    protected State state;

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

        state = new PlayingVideoState();
    }

    public void changeState(State state) {
        this.state = state;
    }

    public void play() {
        videoView.start();
    }

    public void pause() {
        videoView.pause();
    }

    @Override
    public void loadMedia() {
        videoView = (VideoView) mContentView;
        String videoPath = media.getFullPath();

        if (videoPath != null)
            videoView.setVideoPath(videoPath);
    }

    @Override
    public void accept(ActivityVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void initializeButtons() {
        super.initializeButtons();

        LinearLayout layout = findViewById(R.id.videoActionLayout);
        ButtonFactory factory = new PlayPauseButton(layout, R.id.play_pause, state);

        List<ImageButton> buttons = new ArrayList<ImageButton>();
        state.setButtons(buttons);
    }

    @Override
    public void touchScreen() {
        state.touchScreen();
    }
}
