package com.example.cripto_photoaffix.Gallery;

import android.net.Uri;
import com.example.cripto_photoaffix.FileManagement.Deserialazator;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Threads.DecryptorThread;
import com.example.cripto_photoaffix.Threads.EncryptorThread;
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

        List<Queue<EncryptedFile>> queues = divideDecryption();
        List<Media> allMedia = startDecryption(queues, password);

        media.addAll(allMedia);
        allMedia.clear();
        queues.clear();

        Deserialazator.getInstance().free();
    }

    public Gallery(String password, List<Uri> toEncrypt) {
        media = new ArrayList<Media>();

        store(toEncrypt, password);

        List<Queue<EncryptedFile>> queues = divideDecryption();
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
        return media;
    }

    /**
     * Divide el proceso de desencriptado en a lo sumo 5 colas.
     * @return Lista de colas con los archivos a desencriptar.
     */
    private List<Queue<EncryptedFile>> divideDecryption() {
        FilesManager manager = FilesManager.getInstance();
        List<EncryptedFile> encryptedFiles = manager.restoreMedia();

        List<Queue<EncryptedFile>> res = new ArrayList<Queue<EncryptedFile>>();

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

    /**
     * Inicia el proceso de desencriptado en a lo sumo 5 hilos.
     * @param queues Lista de colas de elementos a desencriptar.
     * @param passcode Contraseña con la cual desencriptar los elementos.
     * @return Lista de Media, es decir los archivos desencriptados.
     */
    private List<Media> startDecryption(List<Queue<EncryptedFile>> queues, String passcode) {
        List<DecryptorThread> threads = new ArrayList<DecryptorThread>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        DecryptorThread thread;
        int size = queues.size();
        Queue<EncryptedFile> queue;
        
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
     * Guarda y encripta los archivos de los URIs indicados.
     * @param toEncrypt Lista de archivos a encriptar.
     * @param password Contraseña con la cual encriptar los archivos.
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
     * Divide la tarea de encriptar en a lo sumo 5 colas.
     * @param uris Lista de URIs a encriptar.
     * @return Lista de cola de URIs ya dividida.
     */
    private List<Queue<Uri>> divideEncryption(List<Uri> uris) {

        List<Queue<Uri>> res = new ArrayList<Queue<Uri>>();

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
