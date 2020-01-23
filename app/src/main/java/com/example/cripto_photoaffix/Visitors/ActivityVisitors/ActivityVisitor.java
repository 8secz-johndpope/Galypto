package com.example.cripto_photoaffix.Visitors.ActivityVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.LoginActivity;

public interface ActivityVisitor {
    public void visit(LoginActivity activity);
    public void visit(GalleryActivity activity);
}
