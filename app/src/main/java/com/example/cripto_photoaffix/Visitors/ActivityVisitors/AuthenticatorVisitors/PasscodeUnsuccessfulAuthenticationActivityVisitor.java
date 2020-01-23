package com.example.cripto_photoaffix.Visitors.ActivityVisitors.AuthenticatorVisitors;

import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivity;
import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

public class PasscodeUnsuccessfulAuthenticationActivityVisitor implements ActivityVisitor {
    @Override
    public void visit(LoginActivity activity) {
        activity.loginUnsuccessful();
    }

    @Override
    public void visit(GalleryActivity activity) {}
}
