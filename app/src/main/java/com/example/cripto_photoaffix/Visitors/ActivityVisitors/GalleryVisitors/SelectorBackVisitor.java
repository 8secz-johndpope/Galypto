package com.example.cripto_photoaffix.Visitors.ActivityVisitors.GalleryVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.State;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ImageViewerActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.VideoViewerActivity;
import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

public class SelectorBackVisitor implements ActivityVisitor {
    private State state;

    public SelectorBackVisitor(State state) {
        this.state = state;
    }

    @Override
    public void visit(LoginActivity activity) {}

    @Override
    public void visit(GalleryActivity activity) {
        activity.changeState(state.getNextState());
        activity.unselectAllButtons();
        activity.hideButtons();
    }

    @Override
    public void visit(ImageViewerActivity activity) {}

    @Override
    public void visit(VideoViewerActivity activity) {}
}
