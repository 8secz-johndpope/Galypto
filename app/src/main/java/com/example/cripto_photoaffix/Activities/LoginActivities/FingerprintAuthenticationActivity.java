package com.example.cripto_photoaffix.Activities.LoginActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cripto_photoaffix.Authenticators.FingerprintAuthenticator;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.Factories.PasscodeAuthenticationIntentFactory;
import com.example.cripto_photoaffix.R;

public class FingerprintAuthenticationActivity extends AppCompatActivity {
    FingerprintAuthenticator authenticator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_authentication);

        startAuthentication();
    }

    private void startAuthentication() {
        authenticator = new FingerprintAuthenticator(this);
        authenticator.startAuth();
    }

    public void loadPasscodeAuth(View view) {
        authenticator.cancel();
        IntentFactory factory = new PasscodeAuthenticationIntentFactory(this);
        startActivity(factory.create());
        finish();
    }
}
