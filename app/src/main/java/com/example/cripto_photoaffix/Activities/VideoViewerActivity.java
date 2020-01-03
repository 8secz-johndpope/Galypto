package com.example.cripto_photoaffix.Activities;

import android.os.Bundle;
import android.widget.VideoView;
import com.example.cripto_photoaffix.R;

public class VideoViewerActivity extends ContentViewerActivity {
    protected VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoView = findViewById(R.id.videoView);
    }
}
