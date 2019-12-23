package com.example.cripto_photoaffix.Activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Authenticators.FingerprintAuthenticator;
import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.Factories.RegisterIntentFactory;
import com.example.cripto_photoaffix.FileManagement.TextFilesManager;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.Visitor;

public class LoginActivity extends MyActivity {
    private EditText field;
    private Authenticator passcodeAuthenticator, fingerprintAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializePasswordField();
        choseActivity();
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
        IntentFactory factory = new GalleryIntentFactory(this);

        startActivity(factory.create());

        finish();
    }

    public void loginUnsuccessful() {
        //Vibrate
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
