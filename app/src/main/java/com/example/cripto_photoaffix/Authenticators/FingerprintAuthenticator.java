package com.example.cripto_photoaffix.Authenticators;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Visitors.FingerprintSuccessfulAuthenticationVisitor;
import com.example.cripto_photoaffix.Visitors.FingerprintUnsuccessfulAuthenticationVisitor;
import com.example.cripto_photoaffix.Visitors.Visitor;

import java.util.concurrent.Executor;

public class FingerprintAuthenticator extends Authenticator {
    private BiometricPrompt.PromptInfo promptInfo;

    public FingerprintAuthenticator(MyActivity activity) {
        super(activity);
    }

    public void authenticate() {}

    public boolean canBeUsed() {
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(activity);

        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    public void initialize() {
        Executor executor = ContextCompat.getMainExecutor(activity);
        initializePromptInfo();
        BiometricPrompt prompt = new BiometricPrompt(activity, executor, new MyAuthenticatorCallback());
        prompt.authenticate(promptInfo);
    }

    private void initializePromptInfo() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Touch the fingerprint sensor")
                .setSubtitle("Log in using your fingerprint.").setNegativeButtonText("Use passcode")
                .build();
    }

    class MyAuthenticatorCallback extends BiometricPrompt.AuthenticationCallback {

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Visitor visitor = new FingerprintUnsuccessfulAuthenticationVisitor();
            activity.accept(visitor);
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            Visitor visitor = new FingerprintSuccessfulAuthenticationVisitor();
            activity.accept(visitor);
        }
    }
}
