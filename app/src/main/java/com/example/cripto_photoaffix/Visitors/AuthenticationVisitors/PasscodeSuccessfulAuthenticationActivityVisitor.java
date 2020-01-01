package com.example.cripto_photoaffix.Visitors.AuthenticationVisitors;

import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;

public class PasscodeSuccessfulAuthenticationActivityVisitor implements ActivityVisitor {
    private Authenticator authenticator;

    public PasscodeSuccessfulAuthenticationActivityVisitor(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void visit(LoginActivity activity) {
        activity.loginSuccessful(authenticator.getFinalPassword());
    }
}
