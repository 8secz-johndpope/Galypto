package com.example.cripto_photoaffix.Visitors.ActivityVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ImageViewerActivity;
import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Commands.Command;

public class GalleryButtonVisitor implements ActivityVisitor {
    private Command command;

    public GalleryButtonVisitor(Command command) {
        this.command = command;
    }

    @Override
    public void visit(GalleryActivity activity) {
        activity.executeOnSelected(command);
    }

    @Override
    public void visit(LoginActivity activity) {}

    @Override
    public void visit(ImageViewerActivity activity) {}
}
