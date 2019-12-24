package com.example.cripto_photoaffix.Security;

import java.io.Serializable;

public class EncryptedFile implements Serializable {
    private byte[] data, salt, iv;
    private String fileName;

    public EncryptedFile(byte[] data, byte[] salt, byte[] iv, String fileName) {
        this.data = data;
        this.salt = salt;
        this.iv = iv;
        this.fileName = fileName;
    }

    public EncryptedFile() {
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
}
