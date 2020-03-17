package com.example.cripto_photoaffix.Factories.AuthenticatorsFactories;

import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Authenticators.BiometricsAuthenticator;

public class FingerprintAuthenticatorFactory extends AuthenticatorFactory {

    @Override
    public Authenticator create() {
        return new BiometricsAuthenticator();
    }
}
