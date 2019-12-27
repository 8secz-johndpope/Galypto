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

    public DecryptorThread(Queue<EncryptedFile> encryptedFiles, String passcode) {
        super();
        this.encryptedFiles = encryptedFiles;
        result = new LinkedList<Bitmap>();
        this.passcode = passcode;
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
            bitmapString = encryptor.decrypt(file, passcode);
            bitmap = stringToBitmap(bitmapString);
            result.add(bitmap);
        }
    }

    public List<Bitmap> getBitmaps() {
        return result;
    }

    private Bitmap stringToBitmap(String bit) {
        byte[] bytes = Base64.decode(bit, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }
}
