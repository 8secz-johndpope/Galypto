package com.example.cripto_photoaffix.Activities.RegisterActivities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.AuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.FingerprintAuthenticatorFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Factories.IntentsFactory.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Security.BCrypt;
import com.example.cripto_photoaffix.Security.EncryptedFile;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.Visitor;

import java.security.SecureRandom;

public class RegisterActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText field = findViewById(R.id.passcode);

        field.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    createUserData();

                return true;
            }
        });
    }

    public void accept(Visitor visitor) {}

    private void createUserData() {
        EditText field = findViewById(R.id.passcode);
        String passcode = field.getText().toString();

        String salt = BCrypt.gensalt();
        String password = BCrypt.hashpw(passcode, salt);

        FilesManager manager = new FilesManager(this);
        manager.writeToFile("pswrd", password);

        String finalPassword = generatePassword();
        encryptAndStoreForPassocde(field.getText().toString(), finalPassword);

        AuthenticatorFactory fingerprint = new FingerprintAuthenticatorFactory(this);
        if (fingerprint.create().canBeUsed()) {
            //If the user wants to use fingerprint sensor.
        }

        IntentFactory factory = new GalleryIntentFactory(this);
        startActivity(factory.create());
        finish();
    }

    private String generatePassword() {
        StringBuilder res = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 64; i++) {
            int value = Math.abs((random.nextInt() % 127) + 33);
            while (value < 33 || value > 126)
                value = (value % 127) + 33;

            System.out.println(value + " "  + (char)value);

            char c = (char) value;
            res.append(c);
        }

        return res.toString();
    }

    private void encryptAndStoreForPassocde(String password, String passwordToEncrypt) {
        MyEncryptor encryptor = new MyEncryptor();

        EncryptedFile finalPass = encryptor.encrypt(passwordToEncrypt, password);
        finalPass.setFileName("finalPassword");

        FilesManager manager = new FilesManager(this);
        manager.storePassword(finalPass);
    }

    private void encryptAndStoreForFingerprint(String passwordToEncrypt) {
        
    }
}
