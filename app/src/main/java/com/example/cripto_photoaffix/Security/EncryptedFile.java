package com.example.cripto_photoaffix.Security;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;

import java.io.Serializable;

public abstract class EncryptedFile implements Serializable {
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
        this.data = null;
        this.salt = null;
        this.iv = null;
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

    public abstract void encrypt(String data, String password);

    public abstract String decrypt(String password);

    public abstract Media accept(EncryptedFileVisitor visitor);
}
