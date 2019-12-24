package com.example.cripto_photoaffix.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Authenticators.FingerprintAuthenticator;
import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.Factories.RegisterIntentFactory;
import com.example.cripto_photoaffix.FileManagement.TextFilesManager;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.Visitor;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class LoginActivity extends MyActivity {
    private EditText field;
    private Authenticator passcodeAuthenticator, fingerprintAuthenticator;
    private Queue<Uri> toEncrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toEncrypt = new LinkedTransferQueue<Uri>();

        initializePasswordField();
        choseActivity();
        checkForIncomingIntents();
    }

    private void initializePasswordField() {
        field = findViewById(R.id.loginPasscode);

        field.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    authenticate();

                return true;
            }
        });
    }

    private void checkForIncomingIntents() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/"))
                handleImage(intent);
            else
                handleVideo(intent);
        }
    }

    private void authenticate() {
        passcodeAuthenticator.authenticate();
    }

    private void choseActivity() {
        IntentFactory factory;
        TextFilesManager manager = new TextFilesManager(this);

        if (manager.exists("pswrd")) {
            fingerprintAuthenticator = new FingerprintAuthenticator(this);
            passcodeAuthenticator = new PasscodeAuthenticator(this, field);

            if (fingerprintAuthenticator.canBeUsed())
                fingerprintAuthenticator.initialize();
        }
        else {
            factory = new RegisterIntentFactory(this);
            startActivity(factory.create());
            finish();
        }
    }

    public void loginSuccessful() {
        //Encryptor: encrypt everything in toEncrypt.

        IntentFactory factory = new GalleryIntentFactory(this);
        startActivity(factory.create());

        finish();
    }

    public void loginUnsuccessful() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        else {
            VibrationEffect effect = VibrationEffect.createOneShot(50, 1);
            vibrator.vibrate(effect);
        }
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private void handleImage(Intent intent) {
        //Store and encrypt image.
        Toast.makeText(this, "Image received", Toast.LENGTH_SHORT).show();

        Uri image = intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (image != null)
            toEncrypt.add(image);
    }

    private void handleVideo(Intent intent) {
        //Store and encrypt video.
        Toast.makeText(this, "Video received", Toast.LENGTH_SHORT).show();

        Uri video = intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (video != null)
            toEncrypt.add(video);
    }
}
