package com.example.cripto_photoaffix.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cripto_photoaffix.Authenticators.Authenticator;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.AuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.FingerprintAuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.AuthenticatorsFactories.PasscodeAuthenticatorFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.RegisterIntentFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Security.EncryptedFile;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Threads.DecryptorThread;
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
    private Authenticator authenticator;
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
        encryptQueue(field.getText().toString());

        List<Queue<EncryptedFile>> queues = divideDecryption();
        List<Bitmap> bitmaps = startThreading(queues, field.getText().toString());

        DataTransferer transferer = DataTransferer.getInstance();
        transferer.setData(bitmaps);

        IntentFactory factory = new GalleryIntentFactory(this);
        startActivity(factory.create());

        finish();
    }

    public void loginUnsuccessful() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(75, VibrationEffect.DEFAULT_AMPLITUDE));
        else {
            VibrationEffect effect = VibrationEffect.createOneShot(75, 1);
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
                    passcodeAuthenticate();

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

    private void passcodeAuthenticate() {
        AuthenticatorFactory factory = new PasscodeAuthenticatorFactory(this, field);
        authenticator = factory.create();
        authenticator.authenticate();
    }

    private void choseActivity() {
        IntentFactory factory;
        FilesManager manager = new FilesManager(this);

        if (manager.exists("pswrd")) {
            AuthenticatorFactory authFactory = new FingerprintAuthenticatorFactory(this);
            authenticator = authFactory.create();

            if (authenticator.canBeUsed())
                authenticator.initialize();
            else {
                authFactory = new PasscodeAuthenticatorFactory(this, field);
                authenticator = authFactory.create();
            }
        }
        else {
            manager.removeEverything();
            factory = new RegisterIntentFactory(this);
            startActivity(factory.create());
            finish();
        }
    }

    private void handleImage(Intent intent) {
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

    private List<Queue<EncryptedFile>> divideDecryption() {
        FilesManager manager = new FilesManager(this);
        List<EncryptedFile> encryptedFiles = manager.restoreMedia();
        List<Queue<EncryptedFile>> res = new LinkedList<Queue<EncryptedFile>>();

        int cantQueues = encryptedFiles.size() > 20?20:encryptedFiles.size();
        Queue<EncryptedFile> actual;
        int pos = 0;

        for (int i = 0; i < cantQueues; i++) {
            actual = new LinkedTransferQueue<EncryptedFile>();
            for (int j = 0; j < encryptedFiles.size()/cantQueues; j++) {
                actual.add(encryptedFiles.get(pos));
                pos++;
            }

            res.add(actual);

            if (pos < encryptedFiles.size() - 1 && i == cantQueues - 1) {
                int queue = 0;
                while (queue < cantQueues && pos < encryptedFiles.size()) {
                    res.get(queue).add(encryptedFiles.get(pos));
                    queue++;
                    pos++;
                }
            }
        }

        return res;
    }

    private List<Bitmap> startThreading(List<Queue<EncryptedFile>> queues, String passcode) {
        List<DecryptorThread> threads = new LinkedList<DecryptorThread>();

        Handler handler = new Handler();

        for (Queue<EncryptedFile> queue: queues) {
            DecryptorThread thread = new DecryptorThread(queue, passcode);
            thread.start();
            handler.post(thread);
            threads.add(thread);
        }

        for (Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        List<Bitmap> bitmaps = new LinkedList<Bitmap>();

        for (DecryptorThread thread: threads)
            bitmaps.addAll(thread.getBitmaps());


        return bitmaps;
    }
}
