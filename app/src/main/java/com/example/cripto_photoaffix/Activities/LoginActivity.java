package com.example.cripto_photoaffix.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.AuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.FingerprintAuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.PasscodeAuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.RegisterIntentFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Threads.GalleryInitializerThread;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LoginActivity extends MyActivity {
    private EditText field;
    private Vector<Authenticator> authenticators;
    private List<Uri> toEncrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.view).setVisibility(View.GONE);

        toEncrypt = new ArrayList<Uri>();
        field = findViewById(R.id.loginPasscode);
        authenticators = new Vector<Authenticator>();

        initializeAuthenticators();
        choseActivity();
        checkForIncomingIntents();
    }

    public void loginSuccessful(String password) {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.view).setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getApplicationWindowToken(), 0);

        field.setEnabled(false);

        GalleryInitializerThread galleryInitializer = new GalleryInitializerThread(toEncrypt, password);
        galleryInitializer.start();
    }

    public void loginUnsuccessful() {
        field.selectAll();
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

    @Override
    public void refresh() {}

    private void checkForIncomingIntents() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

            toEncrypt.add(uri);
        }
        else if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {

            ArrayList<Uri> list = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);

            if (list != null)
                toEncrypt.addAll(list);
        }
    }

    private void choseActivity() {
        if (authenticators.isEmpty()) {
            FilesManager manager = FilesManager.getInstance();
            manager.removeEverything();

            IntentFactory factory = new RegisterIntentFactory();

            startActivity(factory.create());

            finish();
        }
    }

    private void initializeAuthenticators() {
        AuthenticatorFactory factory = new PasscodeAuthenticatorFactory(field);
        Authenticator created = factory.create();

        if (created.canBeUsed() && created.filesReady()) {
            authenticators.add(created);

            field.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE)
                        authenticators.get(0).authenticate();

                    return true;
                }
            });
        }

        factory = new FingerprintAuthenticatorFactory();
        created = factory.create();

        if (created.canBeUsed() && created.filesReady())
            authenticators.add(created);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        authenticators.clear();
        authenticators = null;
        field = null;
    }
}
