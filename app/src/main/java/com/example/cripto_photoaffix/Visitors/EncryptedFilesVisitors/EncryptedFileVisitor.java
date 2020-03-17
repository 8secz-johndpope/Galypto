package com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPassword;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedVideo;

public interface EncryptedFileVisitor {
    public Media visit(EncryptedPicture encryptedPicture);
    public Media visit(EncryptedVideo encryptedVideo);
}
