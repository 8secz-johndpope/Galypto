package com.example.cripto_photoaffix.Activities.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.R;

public class PasscodeAuthenticationActivity extends AppCompatActivity {
    private EditText field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode_authentication);

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
}
