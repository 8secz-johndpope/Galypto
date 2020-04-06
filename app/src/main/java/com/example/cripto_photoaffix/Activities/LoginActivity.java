package com.example.cripto_photoaffix.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.cripto_photoaffix.Activities.Dialogs.CannotAddDialog;
import com.example.cripto_photoaffix.Activities.Dialogs.TooManyFilesDialog;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.AuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.BiometricsAuthenticatorFactory;
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
    private final int MAX_SIZE = 50;
    private final int MAX_ELEMENTS = 30;
    private EditText field;
    private CheckBox showPassword;
    private Vector<Authenticator> authenticators;
    private List<Uri> toEncrypt;
    private boolean openingGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.view).setVisibility(View.GONE);

        toEncrypt = new ArrayList<Uri>();
        field = findViewById(R.id.loginPasscode);

        openingGallery = false;

        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticators.get(0).authenticate();
            }
        });

        showPassword = findViewById(R.id.showPassword);
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword.isChecked())
                    field.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else
                    field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
    }

    /**
     * If the login is successful, the visitor calls this method.
     * @param password Password to decrypt files.
     */
    public void loginSuccessful(String password) {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.view).setVisibility(View.VISIBLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Oculta el teclado.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getApplicationWindowToken(), 0);

        CheckBox checkBox = findViewById(R.id.showPassword);

        if (checkBox.isChecked())
            checkBox.performClick();

        field.setEnabled(false);
        Button login = findViewById(R.id.button);
        login.setEnabled(false);

        checkBox.setEnabled(false);

        openingGallery = true;

        GalleryInitializerThread galleryInitializer = new GalleryInitializerThread(toEncrypt, password);
        galleryInitializer.start();
    }

    /**
     * If the password is wrong, the visitor calls this method.
     */
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

    /**
     * Checks if any elements are being shared to the app. If there are elements being shared,
     * they will be added to a queue in order to be encrypted.
     */
    private void checkForIncomingIntents() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        int cannotAdd = 0;

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

            if (getFileSize(uri) < MAX_SIZE)
                toEncrypt.add(uri);
            else
                cannotAdd++;
        }
        else if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {

            ArrayList<Uri> list = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);

            if (list != null) {
                Uri uri;
                int size = list.size();

                for (int i = 0; i < size; i++) {
                    uri = list.get(i);

                    if (getFileSize(uri) < MAX_SIZE)
                        toEncrypt.add(uri);
                    else
                        cannotAdd++;
                }
            }
        }

        if (toEncrypt.size() > MAX_ELEMENTS) {
            AppCompatDialogFragment dialog = new TooManyFilesDialog();
            dialog.show(getSupportFragmentManager(), "Too many files dialog.");
            toEncrypt.clear();
        }
        else {
            if (cannotAdd != 0) {
                AppCompatDialogFragment dialog = new CannotAddDialog(cannotAdd);
                dialog.show(getSupportFragmentManager(), "Cannot add dialog.");
            }
        }
    }

    /**
     * Returns the size of the file in the uri.
     * @param uri URI to which get the size;
     * @return Size in Mb.
     */
    private double getFileSize(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        long filesize = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
        cursor.close();

        return (double)filesize/(1024*1024);
    }

    /**
     * If the user is not registered, the app will open the register activity.
     */
    private void chooseActivity() {
        System.out.println("Path: " + getFilesDir());
        if (authenticators.isEmpty()) {
            FilesManager manager = FilesManager.getInstance();
            manager.removeEverything();

            IntentFactory factory = new RegisterIntentFactory();

            startActivity(factory.create());

            finish();
        }
    }

    /**
     * Initializes the authenticators available.
     */
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

        factory = new BiometricsAuthenticatorFactory();
        created = factory.create();

        if (created.canBeUsed() && created.filesReady())
            authenticators.add(created);
    }

    @Override
    public void onDestroy() {
        authenticators.clear();
        authenticators = null;
        field = null;

        super.onDestroy();
    }

    @Override
    public void onRestart() {
        if (!openingGallery) {
            checkForIncomingIntents();

            for (Authenticator auth : authenticators)
                auth.initialize();
        }

        super.onRestart();
    }

    public void onResume() {
        if (!openingGallery) {
            authenticators = new Vector<Authenticator>();

            checkForIncomingIntents();
            initializeAuthenticators();
            chooseActivity();
        }

        super.onResume();
    }
}
