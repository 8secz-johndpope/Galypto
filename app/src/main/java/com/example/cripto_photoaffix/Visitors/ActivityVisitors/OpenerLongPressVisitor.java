package com.example.cripto_photoaffix.Visitors.ActivityVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.Selector;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.State;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ImageViewerActivity;
import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.MyImageButton;

public class OpenerLongPressVisitor implements ActivityVisitor {
    private MyImageButton longPressed;

    public OpenerLongPressVisitor(MyImageButton button) {
        longPressed = button;
    }

    @Override
    public void visit(GalleryActivity activity) {
        State state = new Selector();
        state.touch(longPressed);
        activity.changeState(state);
        activity.showButtons();
    }

    @Override
    public void visit(LoginActivity activity) {}

    @Override
    public void visit(ImageViewerActivity activity) {}
}
