package com.example.cripto_photoaffix.Security;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;

import java.util.Vector;

public class EncryptedPassword extends EncryptedFile {

    public EncryptedPassword(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedPassword() {
        super();
    }

    public String decrypt(String password) {
        MyEncryptor encryptor = new MyEncryptor();
        return encryptor.decrypt(this, password);
    }

    public void encrypt(String data, String password) {
        MyEncryptor encryptor = new MyEncryptor();
        Vector<byte[]> vector = encryptor.encrypt(data, password);

        this.data = vector.get(0);
        this.iv = vector.get(1);
        this.salt = vector.get(2);
    }

    public Media accept(EncryptedFileVisitor visitor) {
        return visitor.visit(this);
    }
}
