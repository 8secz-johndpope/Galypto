package com.example.cripto_photoaffix.Activities.GalleryActivities.ViewerStates;

import android.widget.ImageButton;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.PlayVisitor;

public class PausedVideoState extends PlayingPausedState {

    public PausedVideoState() {
        nextState = new PlayingVideoState(this);
        visitor = new PlayVisitor();
    }

    public PausedVideoState(State nextState) {
        super(nextState);
        visitor = new PlayVisitor();
    }

    @Override
    public void actOnVideo(ImageButton button) {
        button.setBackgroundResource(R.drawable.pause);
        ActivityTransferer.getInstance().getActivity().accept(visitor);
    }
}
