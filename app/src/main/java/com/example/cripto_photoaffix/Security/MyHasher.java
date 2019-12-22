package com.example.cripto_photoaffix.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class MyHasher {

    public String hash(String password) {
        StringBuilder res = new StringBuilder();
        String salt = getSalt();

        res.append(salt);
        res.append(':');
        res.append(hash(salt, password));

        return res.toString();
    }

    public String getSalt() {
        SecureRandom random = new SecureRandom();

        char[] arr = new char[256];

        for (int i = 0; i < 256; i++) {
            arr[i] = ':';
            while (arr[i] == ':')
                arr[i] = (char)random.nextInt();
        }

        return new String(arr);
    }

    public boolean match(String hash, String password) {
        StringBuilder salt = new StringBuilder();
        StringBuilder onlyHash = new StringBuilder();
        int hashLength = hash.length();

        char c = hash.charAt(0);
        int i = 0;

        while (c != ':' && i < hashLength) {
            salt.append(c);
            i++;
            c = hash.charAt(i);
        }

        i++;

        while (i < hashLength) {
            onlyHash.append(hash.charAt(i));
            i++;
        }

        String tempHash = hash(salt.toString(), password);

        return tempHash.equals(onlyHash.toString());
    }

    private String hash(String salt, String password) {
        StringBuilder total = new StringBuilder();
        total.append(salt);
        total.append(password);
        String res = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            md.update(total.toString().getBytes());

            byte[] bytes = md.digest();

            StringBuilder string = new StringBuilder();
            String temp;

            for (byte b: bytes) {
                temp = Integer.toHexString(b & 0xff);
                string.append(temp);
            }

            res = string.toString();

        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }

        return res;
    }
}

