package com.example.cripto_photoaffix.Gallery;

import android.net.Uri;
import android.os.Handler;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.FileManagement.Deserialazator;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Threads.DecryptorThread;
import com.example.cripto_photoaffix.Threads.EncryptorThread;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class Gallery {
    private List<Media> media;

    public Gallery(String password) {
        media = new LinkedList<Media>();

        List<Queue<EncryptedFile>> queues = divideDecryption();
        List<Media> allMedia = startThreading(queues, password);

        media.addAll(allMedia);
        allMedia.clear();
        queues.clear();

        Deserialazator.getInstance().free();
    }

    public Gallery(String password, List<Uri> toEncrypt) {
        media = new LinkedList<Media>();

        store(toEncrypt, password);

        List<Queue<EncryptedFile>> queues = divideDecryption();
        List<Media> allMedia = startThreading(queues, password);

        media.addAll(allMedia);
        allMedia.clear();
        queues.clear();

        Deserialazator.getInstance().free();
    }

    public Gallery() {
        media = new LinkedList<Media>();
    }

    public List<Media> getMedia() {
        return media;
    }

    private List<Queue<EncryptedFile>> divideDecryption() {
        FilesManager manager = FilesManager.getInstance();
        List<EncryptedFile> encryptedFiles = manager.restoreMedia();

        List<Queue<EncryptedFile>> res = new LinkedList<Queue<EncryptedFile>>();

        int cantQueues = encryptedFiles.size() > 5?5:encryptedFiles.size();

        Queue<EncryptedFile> actual;

        int pos = 0;

        for (int i = 0; i < cantQueues; i++) {

            actual = new LinkedTransferQueue<EncryptedFile>();

            for (int j = 0; j < encryptedFiles.size()/cantQueues; j++) {
                actual.add(encryptedFiles.get(pos));
                pos++;
            }

            res.add(actual);

            if (pos < encryptedFiles.size() && i == cantQueues - 1) {

                int queue = 0;

                while (queue < cantQueues && pos < encryptedFiles.size()) {
                    res.get(queue).add(encryptedFiles.get(pos));
                    queue++;
                    pos++;
                }
            }
        }

        encryptedFiles.clear();

        return res;
    }

    private List<Media> startThreading(List<Queue<EncryptedFile>> queues, String passcode) {
        List<DecryptorThread> threads = new LinkedList<DecryptorThread>();

        Handler handler = new Handler();
        DecryptorThread thread;
        
        for (Queue<EncryptedFile> queue: queues) {
            thread = new DecryptorThread(queue, passcode);
            thread.start();
            handler.post(thread);
            threads.add(thread);
        }

        queues.clear();

        try {

            for (Thread t : threads)
                t.join();

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Media> media = new LinkedList<Media>();

        for (DecryptorThread t: threads) {
            media.addAll(t.getMedia());
            t.clear();
        }

        threads.clear();

        return media;
    }

    private void store(List<Uri> toEncrypt, String password) {
        List<Queue<Uri>> queues = divideEncryption(toEncrypt);
        List<EncryptorThread> threads = new LinkedList<EncryptorThread>();

        EncryptorThread thread;

        for (Queue<Uri> queue: queues) {
            thread = new EncryptorThread(queue, password);
            thread.start();
            threads.add(thread);
        }

        queues.clear();

        try {
            for (Thread t: threads)
                t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EncryptedFile> encryptedFiles = new LinkedList<EncryptedFile>();

        for (EncryptorThread t: threads) {
            encryptedFiles.addAll(t.getEncrypted());
            t.clear();
        }

        threads.clear();

        FilesManager manager = FilesManager.getInstance();
        manager.store(encryptedFiles);
    }

    private List<Queue<Uri>> divideEncryption(List<Uri> uris) {

        List<Queue<Uri>> res = new LinkedList<Queue<Uri>>();

        int cantQueues = uris.size() > 5?5:uris.size();

        Queue<Uri> actual;

        int pos = 0;

        for (int i = 0; i < cantQueues; i++) {

            actual = new LinkedTransferQueue<Uri>();

            for (int j = 0; j < uris.size()/cantQueues; j++) {
                actual.add(uris.get(pos));
                pos++;
            }

            res.add(actual);

            if (pos < uris.size() && i == cantQueues - 1) {

                int queue = 0;

                while (queue < cantQueues && pos < uris.size()) {
                    res.get(queue).add(uris.get(pos));
                    queue++;
                    pos++;
                }
            }
        }

        uris.clear();

        return res;
    }

    public void remove(Media media) {
        this.media.remove(media);
    }
}
