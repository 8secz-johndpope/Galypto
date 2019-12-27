package com.example.cripto_photoaffix.Threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Process;
import android.util.Base64;
import com.example.cripto_photoaffix.Security.EncryptedFile;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DecryptorThread extends Thread {
    private Queue<EncryptedFile> encryptedFiles;
    private List<Bitmap> result;
    private String passcode;
    private boolean finished;

    public DecryptorThread(Queue<EncryptedFile> encryptedFiles) {
        super();
        this.encryptedFiles = encryptedFiles;
        finished = false;
        result = new LinkedList<Bitmap>();
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
        //Run
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        MyEncryptor encryptor = new MyEncryptor();
        String bitmapString;
        Bitmap bitmap;
        EncryptedFile file;

        while (!encryptedFiles.isEmpty()) {
            file = encryptedFiles.poll();
            System.out.println("Decrypting " + file.getFileName());
            bitmapString = encryptor.decrypt(file, passcode);
            bitmap = stringToBitmap(bitmapString);
            result.add(bitmap);
        }

        finished = true;
    }

    public List<Bitmap> getBitmaps() {
        return result;
    }

    public boolean finished() {
        return finished;
    }

    private Bitmap stringToBitmap(String bit) {
        byte[] bytes = Base64.decode(bit, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }
}
