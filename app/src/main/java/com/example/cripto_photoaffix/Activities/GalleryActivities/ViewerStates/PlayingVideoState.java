package com.example.cripto_photoaffix.Activities.GalleryActivities.ViewerStates;

import android.widget.ImageButton;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.PauseVisitor;

public class PlayingVideoState extends PlayingPausedState {

    public PlayingVideoState() {
        nextState = new PausedVideoState(this);
        visitor = new PauseVisitor();
    }

    public PlayingVideoState(State nextState) {
        super(nextState);
        visitor = new PauseVisitor();
    }

    @Override
    public void actOnVideo(ImageButton button) {
        button.setBackgroundResource(R.drawable.pause);
        ActivityTransferer.getInstance().getActivity().accept(visitor);
    }
}
