package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Base64;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedVideo;
import com.example.cripto_photoaffix.Threads.DecryptorThread;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class Gallery {
    private List<Media> media;
    private MyActivity activity;

    public Gallery(MyActivity activity, String password) {
        media = new LinkedList<Media>();
        this.activity = activity;

        List<Queue<EncryptedFile>> queues = divideDecryption();
        List<Media> allMedia = startThreading(queues, password);

        media.addAll(allMedia);
    }

    public Gallery(MyActivity activity, String password, Queue<Uri> picturesToEncrypt, Queue<Uri> videosToEncrypt) {
        media = new LinkedList<Media>();
        this.activity = activity;

        storePictures(picturesToEncrypt, password);
        storeVideos(videosToEncrypt, password);

        List<Queue<EncryptedFile>> queues = divideDecryption();
        List<Media> allMedia = startThreading(queues, password);

        media.addAll(allMedia);
    }

    public Gallery(MyActivity activity) {
        media = new LinkedList<Media>();
        this.activity = activity;
    }

    public List<Media> getMedia() {
        return media;
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

    private List<Media> startThreading(List<Queue<EncryptedFile>> queues, String passcode) {
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

        List<Media> media = new LinkedList<Media>();

        for (DecryptorThread thread: threads)
            media.addAll(thread.getMedia());


        return media;
    }

    private void storePictures(Queue<Uri> toEncrypt, String password) {
        Bitmap bitmap;
        List<EncryptedFile> files = new LinkedList<EncryptedFile>();
        String bitmapString;
        EncryptedFile created;

        while (!toEncrypt.isEmpty()) {
            bitmap = getThumbnail(toEncrypt.poll());
            bitmapString = bitmapToString(bitmap);

            created = new EncryptedPicture();
            created.encrypt(bitmapString, password);

            files.add(created);
        }

        FilesManager manager = new FilesManager(activity);
        manager.store(files);
    }

    private void storeVideos(Queue<Uri> toEncrypt, String password) {
        List<EncryptedFile> files = new LinkedList<EncryptedFile>();
        EncryptedFile created;
        Uri actual;
        String actualContent;

        while (!toEncrypt.isEmpty()) {
            actual = toEncrypt.poll();
            created = new EncryptedVideo();
            actualContent = getDataFromUri(actual);

            created.encrypt(actualContent, password);

            files.add(created);
        }

        FilesManager manager = new FilesManager(activity);
        manager.store(files);
    }

    private Bitmap getThumbnail(Uri uri) {
        Bitmap bitmap;
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);

            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception exception) {
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

    private String getDataFromUri(Uri uri) {
        String res = null;
        try {
            InputStream fis = activity.getContentResolver().openInputStream(uri);

            if (fis != null) {

                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                byte[] bytes = new byte[4096];
                int read = fis.read(bytes);
                System.out.println("Starting to read.");
                while (read != -1) {
                    byteOutputStream.write(bytes);
                    read = fis.read(bytes);
                }
                System.out.println("Finished reading.");

                byte[] data = byteOutputStream.toByteArray();

                res = java.util.Base64.getEncoder().encodeToString(data);

                fis.close();
                byteOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }
}
