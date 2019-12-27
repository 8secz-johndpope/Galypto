package com.example.cripto_photoaffix.Factories.AuthenticatorsFactories;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Authenticators.FingerprintAuthenticator;

public class FingerprintAuthenticatorFactory extends AuthenticatorFactory {
    public FingerprintAuthenticatorFactory(MyActivity activity) {
        super(activity);
    }

    @Override
    public Authenticator create() {
        return new FingerprintAuthenticator(activity);
    }
}
