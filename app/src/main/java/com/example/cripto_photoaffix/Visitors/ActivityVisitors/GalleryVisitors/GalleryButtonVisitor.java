package com.example.cripto_photoaffix.Visitors.ActivityVisitors.GalleryVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ImageViewerActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.VideoViewerActivity;
import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

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

    @Override
    public void visit(VideoViewerActivity activity) {}
}
