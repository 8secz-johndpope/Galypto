package com.example.cripto_photoaffix.Visitors.ActivityVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.Selector;
import com.example.cripto_photoaffix.Activities.LoginActivity;

public class OpenerLongPressVisitor implements ActivityVisitor {
    @Override
    public void visit(GalleryActivity activity) {
        activity.changeState(new Selector());
        activity.showActionButtons();
    }

    @Override
    public void visit(LoginActivity activity) {}
}
