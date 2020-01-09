package com.example.cripto_photoaffix.Factories.IntentsFactory;

import android.content.Intent;

public class ShareIntentFactory extends IntentFactory {
    public ShareIntentFactory() {
        super(null);
    }

    public Intent create() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return intent;
    }
}
