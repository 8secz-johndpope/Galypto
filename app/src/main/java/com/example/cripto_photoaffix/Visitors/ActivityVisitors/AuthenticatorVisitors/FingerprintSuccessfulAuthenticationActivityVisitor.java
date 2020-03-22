package com.example.cripto_photoaffix.Visitors.ActivityVisitors.AuthenticatorVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ImageViewerActivity;
import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

public class FingerprintSuccessfulAuthenticationActivityVisitor implements ActivityVisitor {
    private Authenticator authenticator;

    public FingerprintSuccessfulAuthenticationActivityVisitor(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void visit(LoginActivity activity) {
        activity.loginSuccessful(authenticator.getFinalPassword());
    }

    @Override
    public void visit(GalleryActivity activity) {}

    @Override
    public void visit(ImageViewerActivity activity) {}
}
