package com.example.cripto_photoaffix.Visitors.ActivityVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.State;
import com.example.cripto_photoaffix.Activities.LoginActivity;

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
}
