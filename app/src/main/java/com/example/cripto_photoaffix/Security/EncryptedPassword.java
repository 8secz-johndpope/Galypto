package com.example.cripto_photoaffix.Security;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;

public class EncryptedPassword extends EncryptedFile {

    public EncryptedPassword(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedPassword() {
        super();
    }

    public Media accept(EncryptedFileVisitor visitor) {
        return visitor.visit(this);
    }
}
