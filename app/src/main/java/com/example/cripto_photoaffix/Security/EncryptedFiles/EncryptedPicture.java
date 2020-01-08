package com.example.cripto_photoaffix.Security.EncryptedFiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncryptedPicture extends EncryptedFile {

    public EncryptedPicture(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedPicture() {
        super();
    }

    public Media accept(EncryptedFileVisitor visitor) {
        return visitor.visit(this);
    }

    public String decrypt(String password) {
        MyEncryptor encryptor = new MyEncryptor();
        String data = encryptor.decrypt(this, password);

        Bitmap bitmap = stringToBitmap(data);
        String pth = path + "/" + fileName;
        File file = new File(pth + ".jpg");

        if (!file.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(pth + ".jpg");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pth;
    }

    private Bitmap stringToBitmap(String bit) {
        byte[] bytes = Base64.decode(bit, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }
}
