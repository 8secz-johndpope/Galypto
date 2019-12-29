package com.example.cripto_photoaffix.Security;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MyEncryptor {

    public String decrypt(EncryptedFile file, String password) {
        String decrypted = null;

        try {
            SecretKey secretKey = generateSecretKey(password.toCharArray(), file.getSalt());

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(file.getIV()));

            decrypted = new String(cipher.doFinal(file.getData()), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    public EncryptedFile encrypt(String data, String password) {

        EncryptedFile file = new EncryptedFile();
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(8);

        try {
            SecretKey secretKey = generateSecretKey(password.toCharArray(), salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            AlgorithmParameters parameters = cipher.getParameters();

            byte[] iv = parameters.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] ciphertext = cipher.doFinal(data.getBytes());

            file.setData(ciphertext);
            file.setSalt(salt);
            file.setIV(iv);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    private SecretKey generateSecretKey(char[] password, byte[] salt) {
        SecretKey secretKey = null;

        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey temp = keyFactory.generateSecret(spec);

            secretKey = new SecretKeySpec(temp.getEncoded(), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secretKey;
    }
}
