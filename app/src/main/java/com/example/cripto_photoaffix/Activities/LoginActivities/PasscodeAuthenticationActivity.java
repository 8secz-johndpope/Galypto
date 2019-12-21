package com.example.cripto_photoaffix.Activities.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.R;

public class PasscodeAuthenticationActivity extends AppCompatActivity {
    private Integer passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode_authentication);

        passcode = 0;
    }

    public void press(View view) {
        Button button = (Button) view;
        passcode = passcode * 10 + Integer.parseInt(button.getText().toString());
        System.out.println("Code: " + passcode);
    }

    public void authenticate(View view) {
        PasscodeAuthenticator authenticator = new PasscodeAuthenticator("/");
        boolean auth = authenticator.authenticate(passcode);

        if (auth) {
            IntentFactory factory = new GalleryIntentFactory(this);
            startActivity(factory.create());
            passcode = null;
            finish();
        }
        else {
            passcode = 0;
            System.out.println("Error!");
        }
    }
}
