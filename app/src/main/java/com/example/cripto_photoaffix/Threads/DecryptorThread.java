package com.example.cripto_photoaffix.Threads;

import android.os.Process;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.MediaSelectorVisitor;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DecryptorThread extends Thread {
    private Queue<EncryptedFile> encryptedFiles;
    private List<Media> result;
    private String passcode;

    public DecryptorThread(Queue<EncryptedFile> encryptedFiles, String passcode) {
        super();
        this.encryptedFiles = encryptedFiles;
        result = new LinkedList<Media>();
        this.passcode = passcode;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        EncryptedFile file;
        EncryptedFileVisitor visitor = new MediaSelectorVisitor(passcode);
        Media media;

        while (!encryptedFiles.isEmpty()) {
            file = encryptedFiles.poll();

            media = file.accept(visitor);

            result.add(media);

            file.clear();
        }
    }

    public List<Media> getMedia() {
        return result;
    }

    public void clear() {
        encryptedFiles.clear();
        result.clear();
        passcode = null;
    }
}
