package com.example.cripto_photoaffix;

import com.example.cripto_photoaffix.Flatbuffers.ByteVector;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFileFBS;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPassword;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedVideo;

import java.util.HashMap;
import java.util.Map;

public class Deserialazator {
    private Map<String, EncryptedFile> database;

    public Deserialazator() {
        database = new HashMap<String, EncryptedFile>();
        database.put("image", new EncryptedPicture());
        database.put("video", new EncryptedVideo());
        database.put("text", new EncryptedPassword());
    }

    public EncryptedFile deserialize(EncryptedFileFBS file) {
        EncryptedFile res = database.get(file.type());

        if (res != null) {
            res = res.clone();

            res.setPath(file.path());
            res.setFileName(file.filename());
            res.setData(byteVectorToArray(file.dataVector()));
            res.setSalt(byteVectorToArray(file.saltVector()));
            res.setIV(byteVectorToArray(file.ivVector()));
        }

        return res;
    }

    private byte[] byteVectorToArray(ByteVector vector) {
        byte[] bytes = new byte[vector.length()];
        for (int i = 0; i < vector.length(); i++)
            bytes[i] = vector.get(i);

        return bytes;
    }
}
