package com.example.cripto_photoaffix.Activities.RegisterActivities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;
import com.example.cripto_photoaffix.GalleryTransferer;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.AuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.BiometricsAuthenticatorFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Factories.IntentsFactory.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Gallery.Gallery;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Security.BCrypt;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import java.security.SecureRandom;

public class RegisterActivity extends MyActivity {
    private EditText field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        field = findViewById(R.id.passcode);


        field.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validPassword(field.getText().toString()))
                        createUserData();
                    else {
                        Toast.makeText(ActivityTransferer.getInstance().getActivity(),
                                    "Password must have at least 8 characters and 2 numbers.",
                                        Toast.LENGTH_LONG).show();
                    }
                }

                return true;
            }
        });

        AuthenticatorFactory factory = new BiometricsAuthenticatorFactory();
        factory.create();

        Button register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validPassword(field.getText().toString()))
                    createUserData();
                else {
                    Toast.makeText(ActivityTransferer.getInstance().getActivity(),
                            "Password must have at least 8 characters and 2 numbers.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void accept(ActivityVisitor activityVisitor) {}

    public void refresh() {}

    /**
     * The information provided by the user is stored and the passwords are set up.
     */
    private void createUserData() {
        EditText field = findViewById(R.id.passcode);

        hashPassword(field.getText().toString());

        String finalPassword = generatePassword();

        setupPasscode(field, finalPassword);

        AuthenticatorFactory fingerprintAuthenticatorFactory = new BiometricsAuthenticatorFactory();
        Authenticator fingerprint = fingerprintAuthenticatorFactory.create();

        if (fingerprint.canBeUsed())
            setupBiometrics(fingerprint, finalPassword);

        GalleryTransferer transferer = GalleryTransferer.getInstance();
        transferer.setGallery(new Gallery());

        IntentFactory factory = new GalleryIntentFactory();
        startActivity(factory.create());
        finish();
    }

    /**
     * Generates a "universal password", which will be the one that encrypts and decrypts the files.
     * @return "Universal password".
     */
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

    /**
     * Hashes the user's password and creates the corresponding file.
     * @param password Password to hash.
     */
    private void hashPassword(String password) {
        String salt = BCrypt.gensalt(12);
        String hashed = BCrypt.hashpw(password, salt);

        FilesManager manager = FilesManager.getInstance();

        manager.writeToFile("passcodePassword", hashed);
    }

    /**
     * Encrypts the "universal password" using the user's password.
     * @param field Field with user's password.
     * @param passwordToEncrypt "Universal password" to encrypt.
     */
    private void setupPasscode(EditText field, String passwordToEncrypt) {
        Authenticator authenticator = new PasscodeAuthenticator(field);

        EncryptedFile finalPass = authenticator.encrypt(passwordToEncrypt);
        finalPass.setFileName("passcodeFinalPassword");

        FilesManager manager = FilesManager.getInstance();
        manager.storePassword(finalPass);
    }

    /**
     * Encrypts the "universal password" using biometrics.
     * @param biometric Authenticator containing the user's identification.
     * @param password "Universal password" to encrypt.
     */
    private void setupBiometrics(Authenticator biometric, String password) {
        EncryptedFile file = biometric.encrypt(password);
        file.setFileName("fingerprintFinalPassword");

        FilesManager manager = FilesManager.getInstance();

        manager.storePassword(file);
    }

    /**
     * Decides whether the given password is valid or not.
     * @param password Password to decide.
     * @return True if the password is valid and false if not.
     */
    private boolean validPassword(String password) {
        boolean valid = password.length() > 8;

        if (valid) {
            int cantNumbers = 0;

            for (int i = 0; i < password.length(); i++) {
                if (Character.isDigit(password.charAt(i)))
                    cantNumbers++;
            }

            valid = cantNumbers >= 2;
        }

        return valid;
    }
}
