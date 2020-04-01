package com.example.cripto_photoaffix;

import android.widget.ImageButton;

import androidx.appcompat.widget.AppCompatImageButton;

public abstract class VideoButton extends AppCompatImageButton {
    protected boolean playing;

    protected VideoButton() {
        super(ActivityTransferer.getInstance().getActivity());
        playing = false;
    }

    public boolean isPlaying() {
        return playing;
    }

    public abstract void finishedPlaying();

    public abstract void playing();

    public abstract void paused();
}
