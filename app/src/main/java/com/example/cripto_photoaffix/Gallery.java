package com.example.cripto_photoaffix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Base64;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.EncryptedFile;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Threads.DecryptorThread;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class Gallery {
    private List<Bitmap> pictures;
    private MyActivity activity;

    public Gallery(MyActivity activity, String password) {
        pictures = new LinkedList<Bitmap>();
        this.activity = activity;

        List<Queue<EncryptedFile>> queues = divideDecryption();
        List<Bitmap> bitmaps = startThreading(queues, password);

        pictures.addAll(bitmaps);
    }

    public Gallery(MyActivity activity, String password, Queue<Uri> toEncrypt) {
        pictures = new LinkedList<Bitmap>();
        this.activity = activity;

        storePictures(toEncrypt, password);

        List<Queue<EncryptedFile>> queues = divideDecryption();
        List<Bitmap> bitmaps = startThreading(queues, password);

        pictures.addAll(bitmaps);
    }

    public Gallery(MyActivity activity) {
        pictures = new LinkedList<Bitmap>();
        this.activity = activity;
    }

    public List<Bitmap> getPictures() {
        return pictures;
    }

    private List<Queue<EncryptedFile>> divideDecryption() {
        FilesManager manager = new FilesManager(activity);
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

    private void storePictures(Queue<Uri> toEncrypt, String password) {
        Bitmap bitmap;
        List<EncryptedFile> files = new LinkedList<EncryptedFile>();
        MyEncryptor encryptor = new MyEncryptor();
        String bitmapString;

        while (!toEncrypt.isEmpty()) {
            bitmap = getThumbnail(toEncrypt.poll());
            bitmapString = bitmapToString(bitmap);
            files.add(encryptor.encrypt(bitmapString, password));
        }

        FilesManager manager = new FilesManager(activity);
        manager.store(files);
    }

    private Bitmap getThumbnail(Uri uri) {
        Bitmap bitmap;
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);

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
}
