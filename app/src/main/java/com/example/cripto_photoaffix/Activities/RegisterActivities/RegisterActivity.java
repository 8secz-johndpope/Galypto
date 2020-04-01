package com.example.cripto_photoaffix.Activities.RegisterActivities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cripto_photoaffix.Activities.MyActivity;
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

        AuthenticatorFactory factory = new BiometricsAuthenticatorFactory();
        factory.create();

        Button register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserData();
            }
        });
    }

    /**
     * Accept del visitor. No hace nada ya que por el momento no se requiere que se visite esta
     * actividad.
     * @param activityVisitor Visitor visitando.
     */
    public void accept(ActivityVisitor activityVisitor) {}

    public void refresh() {}

    /**
     * Se crea la informacion brindada por el usuario. Para despues poder ser usada por el login.
     */
    private void createUserData() {
        EditText field = findViewById(R.id.passcode);

        hashPassword(field.getText().toString());

        String finalPassword = generatePassword();

        setupPasscode(field, finalPassword);

        AuthenticatorFactory fingerprintAuthenticatorFactory = new BiometricsAuthenticatorFactory();
        Authenticator fingerprint = fingerprintAuthenticatorFactory.create();

        if (fingerprint.canBeUsed())
            setupFingerprint(fingerprint, finalPassword);

        GalleryTransferer transferer = GalleryTransferer.getInstance();
        transferer.setGallery(new Gallery());

        IntentFactory factory = new GalleryIntentFactory();
        startActivity(factory.create());
        finish();
    }

    /**
     * Genera una contraseña "universal", que va a ser la cual encripta las fotos y videos, ya que
     * la contraseña numerica creada por el usuario obviamente difiere de la biometrica (si es que
     * se usa) y esto nos permite no tener que duplicar los archivos.
     * @return Contraseña "universal".
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
     * Hashea la contraseña y crea el archivo correspondiente.
     * @param password Contraseña a hashear.
     */
    private void hashPassword(String password) {
        String salt = BCrypt.gensalt(12);
        String hashed = BCrypt.hashpw(password, salt);

        FilesManager manager = FilesManager.getInstance();

        manager.writeToFile("passcodePassword", hashed);
    }

    /**
     * Encripta la contraseña "universal" usando la contraseña del usuario.
     * @param field Campo que contiene la contraseña del usuario.
     * @param passwordToEncrypt Contraseña "universal" a encriptar.
     */
    private void setupPasscode(EditText field, String passwordToEncrypt) {
        Authenticator authenticator = new PasscodeAuthenticator(field);

        EncryptedFile finalPass = authenticator.encrypt(passwordToEncrypt);
        finalPass.setFileName("passcodeFinalPassword");

        FilesManager manager = FilesManager.getInstance();
        manager.storePassword(finalPass);
    }

    /**
     * Encripta la contraseña "universal" usando la huella digital.
     * @param fingerprint Authenticador que contiene la contraseña del usuario.
     * @param password Contraseña "universal" a encriptar.
     */
    private void setupFingerprint(Authenticator fingerprint, String password) {
        EncryptedFile file = fingerprint.encrypt(password);
        file.setFileName("fingerprintFinalPassword");

        FilesManager manager = FilesManager.getInstance();

        manager.storePassword(file);
    }
}
