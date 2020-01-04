package com.example.cripto_photoaffix.Activities.RegisterActivities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.AuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.FingerprintAuthenticatorFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Factories.IntentsFactory.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Gallery.Gallery;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Security.BCrypt;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.ActivityVisitor;
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

        AuthenticatorFactory factory = new FingerprintAuthenticatorFactory(this);
        factory.create();
    }

    public void accept(ActivityVisitor activityVisitor) {}

    private void createUserData() {
        EditText field = findViewById(R.id.passcode);

        hashPassword(field.getText().toString());

        String finalPassword = generatePassword();

        encryptAndStoreForPassocde(field, finalPassword);

        AuthenticatorFactory fingerprintAuthenticatorFactory = new FingerprintAuthenticatorFactory(this);
        Authenticator fingerprint = fingerprintAuthenticatorFactory.create();

        if (fingerprint.canBeUsed())
            encryptAndStoreForFingerprint(fingerprint, finalPassword);

        DataTransferer transferer = DataTransferer.getInstance();
        transferer.setData(new Gallery(this));

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

            char c = (char) value;
            res.append(c);
        }

        return res.toString();
    }

    private void hashPassword(String password) {
        String salt = BCrypt.gensalt(12);
        String hashed = BCrypt.hashpw(password, salt);

        System.out.println("Hashed password: " + hashed);
        System.out.println("Password: " + password);

        FilesManager manager = new FilesManager(this);
        manager.createFile("", "passcodePassword");

        manager.writeToFile("passcodePassword", hashed);

        System.out.println(manager.getFileContent("passcodePassword"));
    }

    private void encryptAndStoreForPassocde(EditText field, String passwordToEncrypt) {
        Authenticator authenticator = new PasscodeAuthenticator(this, field);

        EncryptedFile finalPass = authenticator.encrypt(passwordToEncrypt);
        finalPass.setFileName("passcodeFinalPassword");

        FilesManager manager = new FilesManager(this);
        manager.storePassword(finalPass);
    }

    private void encryptAndStoreForFingerprint(Authenticator fingerprint, String password) {
        EncryptedFile file = fingerprint.encrypt(password);
        file.setFileName("fingerprintFinalPassword");

        FilesManager manager = new FilesManager(this);

        manager.storePassword(file);
    }
}
