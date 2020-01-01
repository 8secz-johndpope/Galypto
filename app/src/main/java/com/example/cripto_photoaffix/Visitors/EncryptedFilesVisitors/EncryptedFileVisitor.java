package com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.EncryptedPassword;
import com.example.cripto_photoaffix.Security.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedVideo;

public interface EncryptedFileVisitor {
    public Media visit(EncryptedPicture encryptedPicture);
    public Media visit(EncryptedVideo encryptedVideo);
    public Media visit(EncryptedPassword encryptedPassword);
}
