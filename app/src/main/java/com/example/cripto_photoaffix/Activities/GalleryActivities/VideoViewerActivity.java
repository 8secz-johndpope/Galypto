package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.VideoView;
import com.example.cripto_photoaffix.Factories.ButtonFactories.ButtonFactory;
import com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons.VideoButtons.PlayPauseButtonFactory;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

public class VideoViewerActivity extends ContentViewerActivity {
    protected VideoView videoView;
    private ImageButton play_pause;

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

    public void play() {
        videoView.start();
    }

    public void pause() {
        videoView.pause();
    }

    @Override
    public void loadMedia() {
        videoView = (VideoView) mContentView;

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play_pause.setBackgroundResource(R.drawable.playbutton);
            }
        });

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
        ButtonFactory factory = new PlayPauseButtonFactory(layout, R.id.play_pause);
        play_pause = factory.create();
    }

    @Override
    protected void toggle() {
        super.toggle();

        int visibility = mVisible?View.VISIBLE:View.INVISIBLE;

        play_pause.setVisibility(visibility);
    }
}
