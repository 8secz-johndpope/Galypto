package com.example.cripto_photoaffix.Security.EncryptedFiles;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;

public class EncryptedPicture extends EncryptedFile {

    public EncryptedPicture(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedPicture() {
        super();
    }

    public Media accept(EncryptedFileVisitor visitor) {
        return visitor.visit(this);
    }
}
