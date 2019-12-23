package com.example.cripto_photoaffix.Authenticators;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Visitors.SuccessfulAuthenticationVisitor;
import com.example.cripto_photoaffix.Visitors.UnsuccessfulAuthenticationVisitor;
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
            unsuccessful();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            unsuccessful();
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            Visitor visitor = new SuccessfulAuthenticationVisitor();
            activity.accept(visitor);
        }

        private void unsuccessful() {
            Visitor visitor = new UnsuccessfulAuthenticationVisitor();
            activity.accept(visitor);
        }
    }
}
