package com.example.cripto_photoaffix.Visitors;

import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;

public class FingerprintSuccessfulAuthenticationVisitor implements Visitor {
    private Authenticator authenticator;

    public FingerprintSuccessfulAuthenticationVisitor(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void visit(LoginActivity activity) {
        activity.loginSuccessful(authenticator.getFinalPassword());
    }
}
