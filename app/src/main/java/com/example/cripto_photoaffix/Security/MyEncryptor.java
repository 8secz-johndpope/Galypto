package com.example.cripto_photoaffix.Security;

import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Vector;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MyEncryptor {
    private static final MyEncryptor instance = new MyEncryptor();

    private MyEncryptor() {}

    /**
     * Retorna una instancia del Singleton. Es un singleton para evitar "Memory Churn".
     * @return Instancia del singleton.
     */
    public static MyEncryptor getInstance() {
        return instance;
    }

    /**
     * Retorna informacion desencriptada, dada una contraseña y un archivo a desencriptar.
     * @param file Archivo a desencriptar.
     * @param password Contraseña con la cual desencriptar.
     * @return String con la informacion desencriptada.
     */
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

    /**
     * Retorna un vector que contiene en cada uno de sus componentes:
     *                                  0: Informacion encryptada.
     *                                  1: Vector de inicializacion.
     *                                  2: "Salt".
     * @param data: Informacion a encriptar.
     * @param password: Contraseña con la cual encriptar la informacion.
     * @return Vector con los parametros necesarios para desencriptar.
     */
    public Vector<byte[]> encrypt(String data, String password) {
        Vector<byte[]> res = new Vector<byte[]>(3);
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(8);

        try {
            SecretKey secretKey = generateSecretKey(password.toCharArray(), salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            AlgorithmParameters parameters = cipher.getParameters();

            byte[] iv = parameters.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] ciphertext = cipher.doFinal(data.getBytes());

            res.add(ciphertext);
            res.add(iv);
            res.add(salt);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Genera una contraseña secreta a partir de una contraseña como array de char y una "salt".
     * @param password Contraseña.
     * @param salt "Salt".
     * @return Contraseña secreta.
     */
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
