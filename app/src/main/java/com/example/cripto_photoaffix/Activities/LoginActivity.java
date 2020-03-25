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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.cripto_photoaffix.Activities.Dialogs.CannotAddDialog;
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
    private EditText field;
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
    }

    /**
     * Este metodo es llamado si el usuario ingreso correctamente la contraseña o si las biometricas
     * aceptaron al usuario.
     * @param password Contraseña ingresada por el usuario/biometricas.
     */
    public void loginSuccessful(String password) {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.view).setVisibility(View.VISIBLE);

        //Oculta el teclado.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getApplicationWindowToken(), 0);

        field.setEnabled(false);

        openingGallery = true;

        GalleryInitializerThread galleryInitializer = new GalleryInitializerThread(toEncrypt, password);
        galleryInitializer.start();
    }

    /**
     * Si el usuario ingresa una contraseña incorrecta o las biometricas no lo aceptan, el visitor
     * llama a este metodo.
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

    /**
     * Accept del visitor.
     * @param activityVisitor Visitor que quiere visitar.
     */
    public void accept(ActivityVisitor activityVisitor) {
        activityVisitor.visit(this);
    }

    @Override
    public void refresh() {}

    /**
     * Se fija si se esta tratando de compartir alguna imagen o video con la aplicacion, si es el
     * caso, añade lo que se esta tratando de compartir a una cola. En caso de que uno o mas
     * archivos superen los 60 Mb esos no van a ser agregados a la cola y se va a mostrar un mensaje
     * indicando que no se pueden añadir. Esto es porque el proceso de encriptado consume mucha
     * memoria y requiere poder de procesado, aproximadamente 60Mb es lo que puede aguantar
     * un Galaxy S9 (Mi celular) se deberia encontrar alguna forma mejor para determinar el limite
     * dependiendo de la cantidad del dispositivo en uso.
     */
    private void checkForIncomingIntents() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        int cannotAdd = 0;

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

            if (getFileSize(uri) < 60)
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

                    if (getFileSize(uri) < 60)
                        toEncrypt.add(uri);
                    else
                        cannotAdd++;
                }
            }
        }

        if (cannotAdd != 0) {
            AppCompatDialogFragment dialog = new CannotAddDialog(cannotAdd);
            dialog.show(getSupportFragmentManager(), "Cannot add dialog.");
        }
    }

    /**
     * Retorna el tamaño de un archivo en un URI.
     * @param uri URI a obtener el tamaño.
     * @return Tamaño en Mb del archivo que "apunta" el URI.
     */
    private double getFileSize(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        long filesize = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
        cursor.close();

        return (double)filesize/(1024*1024);
    }

    /**
     * Si el usuario no esta registrado, se inicia la actividad para registrarlo.
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
     * Inicializa los autenticadores que el dispositivo tenga habilitados (no todos, solamente los
     * disponibles por la aplicacion) y el usuario se haya registrado.
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

    /**
     * Si la aplicacion se cierra, se limpian los autenticadores y campos para que las contraseñas,
     * si estan guardadas, sean liberadas de memoria.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        authenticators.clear();
        authenticators = null;
        field = null;
    }

    /**
     * Al reiniciar la actividad, se fija si se estan compartiendo elementos y reinicia los
     * autenticadores.
     */
    @Override
    public void onRestart() {
        super.onRestart();

        if (!openingGallery) {
            checkForIncomingIntents();

            for (Authenticator auth : authenticators)
                auth.initialize();
        }
    }

    /**
     * Inicia los autenticadores si es posible o inicia la activiad de registro
     * finalmente, busca imagenes o videos que se esten compartiendo. Esto se realiza cada
     * vez que se vuelve a la actividad o se inicia.
     */
    public void onResume() {
        super.onResume();

        if (!openingGallery) {
            authenticators = new Vector<Authenticator>();

            initializeAuthenticators();
            chooseActivity();
            checkForIncomingIntents();
        }
    }
}
