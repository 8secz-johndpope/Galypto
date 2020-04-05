package com.example.cripto_photoaffix.Factories.AuthenticatorsFactories;

import com.example.cripto_photoaffix.Authenticators.Authenticator;

public abstract class AuthenticatorFactory {
    /**
     * Creates an authenticator.
     * @return Authenticator created.
     */
    public abstract Authenticator create();
}
