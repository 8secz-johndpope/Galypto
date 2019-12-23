package com.example.cripto_photoaffix.Security;

import com.example.cripto_photoaffix.Activities.MyActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class EncDecryptor {
    private MyActivity activity;

    public EncDecryptor(MyActivity c) {
        activity = c;
    }

    public String decrypt(String password, String file) {
        String res = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);

            SecretKey secretKey = generateKey(password);

            res = readBytes(inputStream, secretKey);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public void encrypt(String password, String data, String destinationFolder, String destination) {
        try {
            createFileIfDoesnotExists(destinationFolder, destination);

            FileOutputStream outFile = new FileOutputStream(destinationFolder + "/"  + destination);

            SecretKey secretKey = generateKey(password);

            writeBytes(data, outFile, secretKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SecretKey generateKey(String password) {
        SecretKey secretKey = null;
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());

            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");//"PBEWithMD5AndTripleDES");
            secretKey = secretKeyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secretKey;
    }

    private void writeBytes(String data, FileOutputStream outputFile, SecretKey secretKey) {
        try {
            byte[] salt = new byte[8];

            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

            Cipher cipher = Cipher.getInstance("PBKDF2WithHmacSHA256");//"PBEWithMD5AndTripleDES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);

            outputFile.write(salt);

      //      byte[] input = new byte[64];
            byte[] bytesRead = data.getBytes("UTF-8");

            outputFile.write(bytesRead);

            byte[] output = cipher.doFinal();
            if (output != null)
                outputFile.write(output);
/*
            byte[] input = new byte[64];
            int bytesRead = data.read(input);

            while (bytesRead != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outputFile.write(output);

                bytesRead = inputFile.read(input);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                outputFile.write(output);

            inputFile.close();*/
            outputFile.flush();
            outputFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readBytes(FileInputStream inputFile, SecretKey secretKey) {
        StringBuilder res = new StringBuilder();
        try {
            byte[] salt = new byte[8];
            inputFile.read(salt);

            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

            Cipher cipher = Cipher.getInstance("PBKDF2WithHmacSHA256");//"PBEWithMD5AndTripleDES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);

            byte[] in = new byte[64];
            int read = inputFile.read(in);
            String reading;

            while (read != -1) {
                byte[] output = cipher.update(in, 0, read);
                if (output != null) {
                    reading = new String(output, "UTF-8");
                    res.append(reading);
                }

                read = inputFile.read(in);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                reading = new String(output, "UTF-8");

            inputFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res.toString();
    }

    private void createFileIfDoesnotExists(String folder, String filename) {
        File path = new File(activity.getFilesDir(), folder);

        if (!path.exists())
            path.mkdirs();

        String [] filesInFolder = path.list();

        boolean exists = false;

        if (filesInFolder != null) {
            int i = 0;

            while (!exists && i < filesInFolder.length) {
                exists = filesInFolder[i].equals(filename);
                i++;
            }
        }

        if (!exists) {
            File file = new File(path, filename);

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
