package com.example.cripto_photoaffix.Factories.AuthenticatorsFactories;

import android.widget.EditText;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;

public class PasscodeAuthenticatorFactory extends AuthenticatorFactory {
    private EditText field;

    public PasscodeAuthenticatorFactory(MyActivity activity, EditText field) {
        super(activity);
        this.field = field;
    }

    public Authenticator create() {
        return new PasscodeAuthenticator(activity, field);
    }
}
