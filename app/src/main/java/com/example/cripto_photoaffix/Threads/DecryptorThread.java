package com.example.cripto_photoaffix.Threads;

import android.os.Process;

import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.MediaSelectorVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DecryptorThread extends Thread {
  //  private Queue<EncryptedFile> encryptedFiles;
  private Queue<String> encryptedFiles;
    private List<Media> result;
    private String passcode;

    /*
    public DecryptorThread(Queue<EncryptedFile> encryptedFiles, String passcode) {
        super();
        this.encryptedFiles = encryptedFiles;
        result = new ArrayList<Media>();
        this.passcode = passcode;
    }*/

    public DecryptorThread(Queue<String> encryptedFiles, String passcode) {
        super();
        this.encryptedFiles = encryptedFiles;
        result = new ArrayList<Media>();
        this.passcode = passcode;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        EncryptedFile file;
        EncryptedFileVisitor visitor = new MediaSelectorVisitor(passcode);
        Media media;

        while (!encryptedFiles.isEmpty()) {
            file = FilesManager.getInstance().restoreFile(encryptedFiles.poll());
            //file = encryptedFiles.poll();

            media = file.accept(visitor);

            result.add(media);

            file.clear();

            try {
                Thread.sleep(25);
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retorna toda la "media" desencriptada.
     * @return Media decrypted.
     */
    public List<Media> getMedia() {
        return result;
    }

    /**
     * Limpia la informacion de memoria.
     */
    public void clear() {
        encryptedFiles.clear();
        result.clear();
        passcode = null;
    }
}
