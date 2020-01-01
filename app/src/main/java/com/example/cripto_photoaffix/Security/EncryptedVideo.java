package com.example.cripto_photoaffix.Security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Vector;

public class EncryptedVideo extends EncryptedFile {

    public EncryptedVideo(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedVideo() {
        super();
    }

    public String decrypt(String password) {
        String finalPath = path + "/" + fileName + "dec.mp4";

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

    public void encrypt(String path, String password) {
        try {
            FileInputStream fis = new FileInputStream(path);

            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

            int read = fis.read();

            while (read != -1) {
                byteOutputStream.write(read);
                read = fis.read();
            }

            byte[] data = byteOutputStream.toByteArray();

            String res = Base64.getEncoder().encodeToString(data);

            MyEncryptor encryptor = new MyEncryptor();
            Vector<byte[]> vector = encryptor.encrypt(res, password);

            this.data = vector.get(0);
            this.iv = vector.get(0);
            this.salt = vector.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
