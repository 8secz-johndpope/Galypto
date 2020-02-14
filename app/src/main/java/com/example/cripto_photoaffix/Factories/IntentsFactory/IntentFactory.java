package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;

public abstract class IntentFactory {
    /**
     * Crea el "Intent" deseado.
     * @return Intent a crear.
     */
    public abstract Intent create();
}
