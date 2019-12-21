package com.example.cripto_photoaffix.Authenticators;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import androidx.core.os.CancellationSignal;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintAuthenticator extends FingerprintManagerCompat.AuthenticationCallback {

    private IntentFactory factory;
    private CancellationSignal cancel;
    private boolean authenticated, failed;

    public FingerprintAuthenticator(Context c) {
        factory = new GalleryIntentFactory(c);
        authenticated = false;
        failed = false;
        cancel = null;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!authenticated)
            failed = true;
    }

    @Override
    public void onAuthenticationFailed() {
        if (!authenticated)
            failed = true;
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {}

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        authenticated = true;
        Context context = factory.getContext();
        context.startActivity(factory.create());
    }

    public void startAuth() {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(factory.getContext());

        cancel = new CancellationSignal();

        fingerprintManagerCompat.authenticate(null, 0, cancel, this, null);
    }

    public void cancel() {
        cancel.cancel();
    }

    public boolean getStatus() {
        return authenticated;
    }

    public boolean failed() {
        return failed;
    }
}