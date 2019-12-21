package com.example.cripto_photoaffix.Encryptors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class EncDecryptor {

    public void decrypt(String password, String file, String destination) {
        try {
            SecretKey secretKey = generateKey(password);

            FileInputStream inputStream = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(destination);

            byte[] salt = new byte[8];
            inputStream.read(salt);

            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);

            byte[] in = new byte[64];
            int read = inputStream.read(in);

            while (read != -1) {
                byte[] output = cipher.update(in, 0, read);
                if (output != null)
                    fos.write(output);

                read = inputStream.read(in);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                fos.write(output);

            inputStream.close();
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encrypt(String password, String data, String destination) {
        try {
            FileInputStream inFile = new FileInputStream(data);
            FileOutputStream outFile = new FileOutputStream(destination);

            SecretKey secretKey = generateKey(password);

            byte[] salt = new byte[8];

            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);

            outFile.write(salt);

            byte[] input = new byte[64];
            int bytesRead = inFile.read(input);

            while (bytesRead != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);

                bytesRead = inFile.read(input);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                outFile.write(output);

            inFile.close();
            outFile.flush();
            outFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SecretKey generateKey(String password) {
        SecretKey secretKey = null;
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());

            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");
            secretKey = secretKeyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secretKey;
    }
}
