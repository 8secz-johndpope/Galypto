package com.example.cripto_photoaffix.Security.EncryptedFiles;

import com.example.cripto_photoaffix.Flatbuffers.FlatBufferBuilder;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;
import java.util.Vector;

public abstract class EncryptedFile {
    protected byte[] data, salt, iv;
    protected String fileName, path;

    protected EncryptedFile(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        this.data = data;
        this.salt = salt;
        this.iv = iv;
        this.fileName = fileName;
        this.path = path;
    }

    protected EncryptedFile() {
        this.data = new byte[10];
        this.salt = new byte[10];
        this.iv = new byte[10];
        this.fileName = "";
        this.path = "";
    }

    public String decrypt(String password) {
        MyEncryptor encryptor = MyEncryptor.getInstance();
        return encryptor.decrypt(this, password);
    }

    public void encrypt(String data, String passowrd) {
        MyEncryptor encryptor = MyEncryptor.getInstance();
        Vector<byte[]> vector = encryptor.encrypt(data, passowrd);

        this.data = vector.get(0);
        this.iv = vector.get(1);
        this.salt = vector.get(2);

        vector.removeAllElements();
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIV() {
        return iv;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    public void setFileName(String name) {
        fileName = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public abstract Media accept(EncryptedFileVisitor visitor);

    public abstract EncryptedFile clone();

    public abstract FlatBufferBuilder serialize();
}
