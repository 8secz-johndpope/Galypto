package com.example.cripto_photoaffix.Security.EncryptedFiles;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;
import java.io.FileOutputStream;
import java.util.Base64;

public class EncryptedVideo extends EncryptedFile {
    public EncryptedVideo(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedVideo() {
        super();
    }

    public String decrypt(String password) {
        String finalPath = path + "/" + fileName + ".mp4";

        try {

            MyEncryptor encryptor = new MyEncryptor();

            String data = encryptor.decrypt(this, password);
            byte[] bytes = Base64.getDecoder().decode(data);

            FileOutputStream fos = new FileOutputStream(finalPath);

            fos.write(bytes);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            finalPath = null;
        }

        return finalPath;
    }

    public Media accept(EncryptedFileVisitor visitor) {
        return visitor.visit(this);
    }
}
