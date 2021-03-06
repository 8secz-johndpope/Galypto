package com.example.cripto_photoaffix.Gallery;

import android.net.Uri;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.DeleteCommand;
import com.example.cripto_photoaffix.FileManagement.Deserialazator;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Threads.DecryptorThread;
import com.example.cripto_photoaffix.Threads.EncryptorThread;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Gallery {
    private List<Media> media;

    public Gallery(String password) {
        media = new ArrayList<Media>();

        List<Queue<String>> queues = divideDecryption();
        List<Media> allMedia = startDecryption(queues, password);

        media.addAll(allMedia);
        allMedia.clear();
        queues.clear();

        Deserialazator.getInstance().free();
    }

    public Gallery(String password, List<Uri> toEncrypt) {
        media = new ArrayList<Media>();

        store(toEncrypt, password);

        List<Queue<String>> queues = divideDecryption();
        List<Media> allMedia = startDecryption(queues, password);

        media.addAll(allMedia);
        allMedia.clear();
        queues.clear();

        Deserialazator.getInstance().free();
    }

    public Gallery() {
        media = new ArrayList<Media>();
    }

    public List<Media> getMedia() {
        Queue<Media> toRemove = new ArrayDeque<Media>();

        int size = media.size();
        Media actual;

        for (int i = 0; i < size; i++) {
            actual = media.get(i);

            if (!FilesManager.getInstance().exists(actual.getFullPath()))
                toRemove.add(actual);
        }

        while (!toRemove.isEmpty())
            media.remove(toRemove.poll());

        return new ArrayList<Media>(media);
    }

    /**
     * Divides the decryption process in as much 5 queues.
     * @return List of queues with files to decrypt.
     */
    private List<Queue<String>> divideDecryption() {
        FilesManager manager = FilesManager.getInstance();
        List<String> encryptedFiles = manager.getMedia();

        List<Queue<String>> res = new ArrayList<Queue<String>>();

        int cantQueues = Math.min(encryptedFiles.size(), 5);

        Queue<String> actual;

        int pos = 0;

        for (int i = 0; i < cantQueues; i++) {

            actual = new LinkedTransferQueue<String>();

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

    /**
     * Initializes the decryption process in as much 5 threads.
     * @param queues List of queues with elements to decrypt.
     * @param passcode Password to decrypt the files.
     * @return List of media with the files decrypted.
     */
    private List<Media> startDecryption(List<Queue<String>> queues, String passcode) {
        List<DecryptorThread> threads = new ArrayList<DecryptorThread>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        DecryptorThread thread;
        int size = queues.size();
        Queue<String> queue;

        for (int i = 0; i < size; i++) {
            queue = queues.get(i);

            thread = new DecryptorThread(queue, passcode);
            executor.execute(thread);
            threads.add(thread);
        }

        queues.clear();

        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Media> media = new ArrayList<Media>();

        for (int i = 0; i < size; i++) {
            thread = threads.get(i);

            media.addAll(thread.getMedia());
            thread.clear();
        }

        threads.clear();

        return media;
    }

    /**
     * Stores and ecrypts a list of uris.
     * @param toEncrypt List of uris to encrypt and store.
     * @param password Password to encrypt them.
     */
    private void store(List<Uri> toEncrypt, String password) {
        List<Queue<Uri>> queues = divideEncryption(toEncrypt);
        List<EncryptorThread> threads = new ArrayList<EncryptorThread>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        EncryptorThread thread;

        int size = queues.size();
        Queue<Uri> queue;

        for (int i = 0; i < size; i++) {
            queue = queues.get(i);

            thread = new EncryptorThread(queue, password);
            executor.execute(thread);
            threads.add(thread);
        }

        queues.clear();

        size = threads.size();

        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EncryptedFile> encryptedFiles = new ArrayList<EncryptedFile>();

        for (int i = 0; i < size; i++) {
            thread = threads.get(i);

            encryptedFiles.addAll(thread.getEncrypted());
            thread.clear();
        }

        threads.clear();

        FilesManager manager = FilesManager.getInstance();
        manager.store(encryptedFiles);
    }

    /**
     * Divided the encryption task in as much 5 queues.
     * @param uris List of URIs to encrypt.
     * @return List of queues with Uris to encrypt.
     */
    private List<Queue<Uri>> divideEncryption(List<Uri> uris) {

        List<Queue<Uri>> res = new ArrayList<Queue<Uri>>();

        int cantQueues = Math.min(uris.size(), 5);

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

    /**
     * Deletes media from the gallery.
     * @param media Media to delete.
     */
    public void remove(Media media) {
        Command command = new DeleteCommand();
        command.addMedia(media);
        command.execute();

        this.media.remove(media);
    }
}
