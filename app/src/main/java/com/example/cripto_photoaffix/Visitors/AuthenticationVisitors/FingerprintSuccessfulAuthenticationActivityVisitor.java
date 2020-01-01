package com.example.cripto_photoaffix.Visitors.AuthenticationVisitors;

import com.example.cripto_photoaffix.Activities.LoginActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;

public class FingerprintSuccessfulAuthenticationActivityVisitor implements ActivityVisitor {
    private Authenticator authenticator;

    public FingerprintSuccessfulAuthenticationActivityVisitor(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void visit(LoginActivity activity) {
        activity.loginSuccessful(authenticator.getFinalPassword());
    }
}
