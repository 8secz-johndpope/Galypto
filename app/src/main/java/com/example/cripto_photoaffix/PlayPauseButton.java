package com.example.cripto_photoaffix;

public class PlayPauseButton extends VideoButton {
    public PlayPauseButton() {
        super();
    }

    @Override
    public void finishedPlaying() {
        setBackgroundResource(R.drawable.play);
    }

    @Override
    public void playing() {
        setBackgroundResource(R.drawable.pause);
    }

    @Override
    public void paused() {
        setBackgroundResource(R.drawable.play);
    }
}
