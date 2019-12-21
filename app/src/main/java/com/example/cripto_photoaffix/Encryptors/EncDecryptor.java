package com.example.cripto_photoaffix.Encryptors;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class EncDecryptor {

    public void decrypt(String passwd, String file, String destination) {
        SecretKeySpec key = new SecretKeySpec(passwd.getBytes(), "AES");
        byte[] bytes = new byte[8];
        try {
            FileInputStream inputStream = new FileInputStream(file);
            FileOutputStream outputStream = new FileOutputStream(destination);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);

            int byt = cipherInputStream.read(bytes);

            StringBuilder res = new StringBuilder();

            while (byt != -1) {
                outputStream.write(bytes, 0, byt);
                byt = cipherInputStream.read(bytes);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();
            cipherInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encrypt(String passwd, String data, String destination) {
        SecretKeySpec key = new SecretKeySpec(passwd.getBytes(), "AES");

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            FileOutputStream output = new FileOutputStream(destination);
            FileInputStream inputStream = new FileInputStream(data);

            CipherOutputStream stream = new CipherOutputStream(output, cipher);

            byte[] bytes = new byte[8];
            int byt = inputStream.read(bytes);

            while (byt != -1) {
                stream.write(bytes, 0, byt);
                byt = inputStream.read(bytes);
            }

            output.flush();
            output.close();
            inputStream.close();
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
