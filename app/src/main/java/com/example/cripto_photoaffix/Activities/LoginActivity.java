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
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.AuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.FingerprintAuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.PasscodeAuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.RegisterIntentFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.Visitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class LoginActivity extends MyActivity {
    private EditText field;
    private Authenticator authenticator;
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

    public void loginSuccessful(String password) {
        Gallery gallery;

        if (toEncrypt.isEmpty())
            gallery = new Gallery(this, password);
        else
            gallery = new Gallery(this, password, toEncrypt);

        DataTransferer transferer = DataTransferer.getInstance();
        transferer.setData(gallery);

        IntentFactory factory = new GalleryIntentFactory(this);
        startActivity(factory.create());

        finish();
    }

    public void loginUnsuccessful() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(75, VibrationEffect.DEFAULT_AMPLITUDE));
        else {
            VibrationEffect effect = VibrationEffect.createOneShot(75, 1);
            vibrator.vibrate(effect);
        }
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private void initializePasswordField() {
        field = findViewById(R.id.loginPasscode);

        field.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    authenticator.authenticate();

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

    private void choseActivity() {
        IntentFactory factory;
        FilesManager manager = new FilesManager(this);

        boolean allFilesExist = manager.exists(getFilesDir() + "/passcodeFinalPassword") &&
                manager.exists(getFilesDir() + "/passcodePassword");


        AuthenticatorFactory factory1 = new FingerprintAuthenticatorFactory(this);
        if (allFilesExist && factory1.create().canBeUsed())
            allFilesExist = manager.exists(getFilesDir() + "/fingerprintFinalPassword");

        if (allFilesExist)
            initializeAuthenticators();
        else {
            manager.removeEverything();
            factory = new RegisterIntentFactory(this);
            startActivity(factory.create());
            finish();
        }
    }

    private void initializeAuthenticators() {
        AuthenticatorFactory factory = new PasscodeAuthenticatorFactory(this, field);
        Authenticator created = factory.create();

        if (created.canBeUsed())
            authenticator = created;

        factory = new FingerprintAuthenticatorFactory(this);
        factory.create();
    }

    private void handleImage(Intent intent) {
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
