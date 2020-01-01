package com.example.cripto_photoaffix.Visitors.AuthenticationVisitors;

import com.example.cripto_photoaffix.Activities.LoginActivity;

public class PasscodeUnsuccessfulAuthenticationActivityVisitor implements ActivityVisitor {
    @Override
    public void visit(LoginActivity activity) {
        activity.loginUnsuccessful();
    }
}
