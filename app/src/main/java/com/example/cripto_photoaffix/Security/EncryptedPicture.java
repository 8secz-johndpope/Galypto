package com.example.cripto_photoaffix.Security;

import java.util.Vector;

public class EncryptedPicture extends EncryptedFile {

    public EncryptedPicture(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedPicture() {
        super();
    }

    public String decrypt(String password) {
        MyEncryptor encryptor = new MyEncryptor();
        return encryptor.decrypt(this, password);
    }

    public void encrypt(String data, String passowrd) {
        MyEncryptor encryptor = new MyEncryptor();
        Vector<byte[]> vector = encryptor.encrypt(data, passowrd);
        this.data = vector.get(0);
        this.iv = vector.get(1);
        this.salt = vector.get(2);
    }
}
