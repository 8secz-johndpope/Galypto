package com.example.cripto_photoaffix.FileManagement;

import com.example.cripto_photoaffix.Flatbuffers.ByteVector;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFileFBS;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPassword;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedVideo;
import java.util.HashMap;
import java.util.Map;

public class Deserialazator {
    private static Map<String, EncryptedFile> database;
    private static final Deserialazator instance = new Deserialazator();

    private Deserialazator() {
        initialize();
    }

    /**
     * Initializes the database.
     */
    private static void initialize() {
        database = new HashMap<String, EncryptedFile>();
        database.put("image", new EncryptedPicture());
        database.put("video", new EncryptedVideo());
        database.put("text", new EncryptedPassword());
    }

    /**
     * Returns an instance of the deserializator.
     * @return Instance.
     */
    public static Deserialazator getInstance() {
        if (database == null)
            initialize();

        return instance;
    }

    /**
     * Deserializes a serialized file.
     * @param file Serialized file.
     * @return EncryptedFile (deserialized).
     */
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

    /**
     * Once finished using, the deserializator has to be freed..
     */
    public void free() {
        database.clear();
        database = null;
    }

    /**
     * Transforms a ByteVector into a byte array.
     * @param vector ByteVector to transform.
     * @return ByteVector transformed.
     */
    private byte[] byteVectorToArray(ByteVector vector) {
        byte[] bytes = new byte[vector.length()];
        for (int i = 0; i < vector.length(); i++)
            bytes[i] = vector.get(i);

        return bytes;
    }
}
