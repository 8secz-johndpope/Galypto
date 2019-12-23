package com.example.cripto_photoaffix.Visitors;

import android.util.Log;

import com.example.cripto_photoaffix.Activities.LoginActivity;

public class SuccessfulAuthenticationVisitor implements Visitor {

    public void visit(LoginActivity activity) {
        activity.loginSuccessful();
    }
}
