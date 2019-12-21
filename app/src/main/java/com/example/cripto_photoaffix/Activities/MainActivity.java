package com.example.cripto_photoaffix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.cripto_photoaffix.Activities.LoginActivities.FingerprintAuthenticationActivity;
import com.example.cripto_photoaffix.Activities.LoginActivities.PasscodeAuthenticationActivity;
import com.example.cripto_photoaffix.Factories.FingerprintAuthenticationIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.Factories.PasscodeAuthenticationIntentFactory;
import com.example.cripto_photoaffix.R;

public class MainActivity extends AppCompatActivity {
    private FingerprintManagerCompat fingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFactory factory;

        if (canUseFingerprint())
            factory = new FingerprintAuthenticationIntentFactory(this);
        else
            factory = new PasscodeAuthenticationIntentFactory(this);

        startActivity(factory.create());
        finish();
    }

    private boolean canUseFingerprint() {
        boolean res = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        if (res) {
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = FingerprintManagerCompat.from(this);

            res = ContextCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC)
                  == PackageManager.PERMISSION_GRANTED
                  && fingerprintManager.isHardwareDetected()
                  && fingerprintManager.hasEnrolledFingerprints()
                  && keyguardManager.isDeviceSecure();

        }

        return res;
    }
}
