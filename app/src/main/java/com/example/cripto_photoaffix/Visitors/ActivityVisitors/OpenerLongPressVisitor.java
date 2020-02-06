package com.example.cripto_photoaffix.Visitors.ActivityVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.Selector;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.State;
import com.example.cripto_photoaffix.Activities.LoginActivity;

public class OpenerLongPressVisitor implements ActivityVisitor {
    private State state;

    public OpenerLongPressVisitor(State state) {
        this.state = state;
    }

    @Override
    public void visit(GalleryActivity activity) {
        activity.changeState(new Selector(state));
        activity.showActionButtons();
    }

    @Override
    public void visit(LoginActivity activity) {}
}
