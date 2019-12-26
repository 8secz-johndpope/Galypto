package com.example.cripto_photoaffix.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.Authenticators.FingerprintAuthenticator;
import com.example.cripto_photoaffix.Authenticators.PasscodeAuthenticator;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentFactory;
import com.example.cripto_photoaffix.Factories.RegisterIntentFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Security.EncryptedFile;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.Visitor;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class LoginActivity extends MyActivity {
    private EditText field;
    private Authenticator passcodeAuthenticator, fingerprintAuthenticator;
    private Queue<Uri> toEncrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toEncrypt = new LinkedTransferQueue<Uri>();

        initializePasswordField();
        choseActivity();
        checkForIncomingIntents();
    }

    public void loginSuccessful() {
        //Encryptor: encrypt everything in toEncrypt.
        encryptQueue(field.getText().toString());

        List<Bitmap> bitmaps = decryptFiles();
        DataTransferer transferer = DataTransferer.getInstance();
        transferer.setData(bitmaps);

        IntentFactory factory = new GalleryIntentFactory(this);
        startActivity(factory.create());

        finish();
    }

    public void loginUnsuccessful() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        else {
            VibrationEffect effect = VibrationEffect.createOneShot(50, 1);
            vibrator.vibrate(effect);
        }
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
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

    private void checkForIncomingIntents() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/"))
                handleImage(intent);
            else
                handleVideo(intent);
        }
    }

    private void authenticate() {
        passcodeAuthenticator.authenticate();
    }

    private void choseActivity() {
        IntentFactory factory;
        FilesManager manager = new FilesManager(this);

        if (manager.exists("pswrd")) {
            fingerprintAuthenticator = new FingerprintAuthenticator(this);
            passcodeAuthenticator = new PasscodeAuthenticator(this, field);

            if (fingerprintAuthenticator.canBeUsed())
                fingerprintAuthenticator.initialize();
        }
        else {
            manager.removeEverything();
            factory = new RegisterIntentFactory(this);
            startActivity(factory.create());
            finish();
        }
    }

    private void handleImage(Intent intent) {
        //Store and encrypt image.
        Toast.makeText(this, "Image received", Toast.LENGTH_SHORT).show();

        Uri image = intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (image != null)
            toEncrypt.add(image);

    }

    private void handleVideo(Intent intent) {
        //Store and encrypt video.
        Toast.makeText(this, "Video received", Toast.LENGTH_SHORT).show();

        Uri video = intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (video != null)
            toEncrypt.add(video);
    }

    private void encryptQueue(String password) {
        System.out.println("ENCRYPTING QUEUEUE");
        Bitmap bitmap;
        List<EncryptedFile> files = new LinkedList<EncryptedFile>();
        MyEncryptor encryptor = new MyEncryptor();
        String bitmapString;

        while (!toEncrypt.isEmpty()) {
            bitmap = getThumbnail(toEncrypt.poll());
            bitmapString = bitmapToString(bitmap);
            files.add(encryptor.encrypt(bitmapString, password));
        }

        FilesManager manager = new FilesManager(this);
        manager.store(files);
    }

    private List<Bitmap> decryptFiles() {
        FilesManager manager = new FilesManager(this);
        List<EncryptedFile> encryptedFiles = manager.restoreMedia();
        MyEncryptor encryptor = new MyEncryptor();
        String bitmapString;
        Bitmap bitmap;
        List<Bitmap> bitmaps = new LinkedList<Bitmap>();

        for (EncryptedFile file: encryptedFiles) {
            System.out.println("FILE NAME: "  + file.getFileName());
            System.out.println("FILE SALT: "  + file.getSalt());
            System.out.println("FILE DATA: " + file.getData());
            bitmapString = encryptor.decrypt(file, field.getText().toString());
            bitmap = stringToBitmap(bitmapString);
            bitmaps.add(bitmap);
        }

        return bitmaps;
    }

    private Bitmap getThumbnail(Uri uri) {
        Bitmap bitmap;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);

            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            bitmap = null;
        }

        return bitmap;
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        byte[] bytes = outputStream.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private Bitmap stringToBitmap(String bit) {
        byte[] bytes = Base64.decode(bit, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }
}
