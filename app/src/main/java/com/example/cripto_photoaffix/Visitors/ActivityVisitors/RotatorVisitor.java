package com.example.cripto_photoaffix.Visitors.ActivityVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ImageViewerActivity;
import com.example.cripto_photoaffix.Activities.LoginActivity;

public class RotatorVisitor implements ActivityVisitor {
    private float degrees;

    public RotatorVisitor(float degrees) {
        this.degrees = degrees;
    }

    @Override
    public void visit(GalleryActivity activity) {}

    @Override
    public void visit(LoginActivity activity) {}

    @Override
    public void visit(ImageViewerActivity activity) {
        activity.rotateImage(degrees);
    }
}
