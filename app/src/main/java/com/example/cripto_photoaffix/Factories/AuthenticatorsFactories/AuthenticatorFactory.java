package com.example.cripto_photoaffix.Factories.AuthenticatorsFactories;

import com.example.cripto_photoaffix.Authenticators.Authenticator;

public abstract class AuthenticatorFactory {
    /**
     * Crea un autenticador.
     * @return Autenticador especificado.
     */
    public abstract Authenticator create();
}
