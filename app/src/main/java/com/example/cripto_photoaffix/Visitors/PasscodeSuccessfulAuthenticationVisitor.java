package com.example.cripto_photoaffix.Visitors;

import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;

public class PasscodeSuccessfulAuthenticationVisitor implements Visitor {
    private Authenticator authenticator;

    public PasscodeSuccessfulAuthenticationVisitor(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void visit(LoginActivity activity) {
        activity.loginSuccessful(authenticator.getFinalPassword());
    }
}
