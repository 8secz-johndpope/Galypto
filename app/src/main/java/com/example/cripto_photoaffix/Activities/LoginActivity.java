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
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.AuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.FingerprintAuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.PasscodeAuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.RegisterIntentFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Gallery;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.ActivityVisitor;
import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class LoginActivity extends MyActivity {
    private EditText field;
    private Authenticator authenticator;
    private Queue<Uri> picturesToEncrypt, videosToEncrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        picturesToEncrypt = new LinkedTransferQueue<Uri>();
        videosToEncrypt = new LinkedTransferQueue<Uri>();

        initializePasswordField();
        choseActivity();
        checkForIncomingIntents();
    }

    public void loginSuccessful(String password) {
        Gallery gallery;

        if (picturesToEncrypt.isEmpty() && videosToEncrypt.isEmpty())
            gallery = new Gallery(this, password);
        else
            gallery = new Gallery(this, password, picturesToEncrypt, videosToEncrypt);

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

    public void accept(ActivityVisitor activityVisitor) {
        activityVisitor.visit(this);
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
                handleImage((Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM));
            else
                handleVideo((Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM));
        }
        else if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
            ArrayList<Uri> list = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            try {
                for (Uri u : list) {
                    if (u.getPath().endsWith(".mp4"))
                        handleVideo(u);
                    else
                        handleImage(u);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private void handleImage(Uri image) {
   //     Uri image = intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (image != null)
            picturesToEncrypt.add(image);

    }

    private void handleVideo(Uri video) {
 //       Uri video = intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (video != null)
            videosToEncrypt.add(video);
    }
}
