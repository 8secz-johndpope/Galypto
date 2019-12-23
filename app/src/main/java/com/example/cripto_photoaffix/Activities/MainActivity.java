package com.example.cripto_photoaffix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.Factories.RegisterIntentFactory;
import com.example.cripto_photoaffix.FileManagement.TextFilesManager;
import com.example.cripto_photoaffix.R;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private BiometricPrompt.PromptInfo promptInfo;
    private EditText field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFactory factory;

        TextFilesManager manager = new TextFilesManager(this);

        initializePasswordField();

        if (manager.exists("pswrd")) {
            if (canUseFingerprint())
                initializeFingerprintAuthentication();
               // factory = new FingerprintAuthenticationIntentFactory(this);
        }
        else {
            factory = new RegisterIntentFactory(this);
            startActivity(factory.create());
            finish();
        }
    }

    private boolean canUseFingerprint() {
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);

        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    private void initializeFingerprintAuthentication() {
        Executor executor = ContextCompat.getMainExecutor(this);
        initializePromptInfo();
        BiometricPrompt prompt = new BiometricPrompt(this, executor, new MyAuthenticatorCallback(this));
        prompt.authenticate(promptInfo);
    }

    private void initializePasswordField() {
        field = findViewById(R.id.loginPasscode);

        field.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    authenticate(field.getText().toString());

                return true;
            }
        });
    }

    private void initializePromptInfo() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Touch the fingerprint sensor")
                .setSubtitle("Log in using your fingerprint.").setNegativeButtonText("Use passcode")
                .build();
    }

    private void authenticate(String passcode) {
        PasscodeAuthenticator authenticator = new PasscodeAuthenticator(this);
        boolean auth = authenticator.authenticate(passcode);

        if (auth) {
            IntentFactory factory = new GalleryIntentFactory(this);
            startActivity(factory.create());
            finish();
        }
        else {
            field.selectAll();
            System.out.println("Error!");
        }
    }

    class MyAuthenticatorCallback extends BiometricPrompt.AuthenticationCallback {
        private IntentFactory factory;

        public MyAuthenticatorCallback(Context context) {
            factory = new GalleryIntentFactory(context);
        }

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            Context context = factory.getContext();
            context.startActivity(factory.create());
            finish();
        }
    }
}
