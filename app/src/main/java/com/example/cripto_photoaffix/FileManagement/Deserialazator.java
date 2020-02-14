package com.example.cripto_photoaffix.FileManagement;

import com.example.cripto_photoaffix.Flatbuffers.ByteVector;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFileFBS;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPassword;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedVideo;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase tiene como objetivo poder deserializar los archivos serializados de una forma que sea
 * facil modificar en caso de añadir otros tipos de "Media", ya que Flatbuffers no permite la
 * jerarquia de herencia. Esto flexibiliza esta tarea, ya que basta con añadir al mapeo el tipo
 * nuevo de "Media".
 */
public class Deserialazator {
    private static Map<String, EncryptedFile> database;
    private static final Deserialazator instance = new Deserialazator();

    private Deserialazator() {
        initialize();
    }

    /**
     * Iicializa el mapeo que determina de que tipo es cada archivo encriptado serializado.
     */
    private static void initialize() {
        database = new HashMap<String, EncryptedFile>();
        database.put("image", new EncryptedPicture());
        database.put("video", new EncryptedVideo());
        database.put("text", new EncryptedPassword());
    }

    /**
     * Retorna instancia del deserializador ya que es un Singleton. Es un Singleton para evitar
     * "Memory Churn".
     * @return Instancia actual.
     */
    public static Deserialazator getInstance() {
        if (database == null)
            initialize();

        return instance;
    }

    /**
     * Retorna el archivo de Flatbuffers deserializado.
     * @param file Archivo encriptado serializado por Flatbuffers.
     * @return Archivo encriptado (deserializado).
     */
    public EncryptedFile deserialize(EncryptedFileFBS file) {
        EncryptedFile res = database.get(file.type());
        long start = System.currentTimeMillis();

        if (res != null) {
            res = res.clone();

            res.setPath(file.path());
            res.setFileName(file.filename());
            res.setData(byteVectorToArray(file.dataVector()));
            res.setSalt(byteVectorToArray(file.saltVector()));
            res.setIV(byteVectorToArray(file.ivVector()));
        }

        long finish = System.currentTimeMillis();

        System.out.println("It took " + (double)(finish-start)/1000 + " seconds.");

        return res;
    }

    /**
     * Una vez terminado de usar se libera de la memoria.
     */
    public void free() {
        database.clear();
        database = null;
    }

    /**
     * Transforma un ByteVector a array de bytes.
     * @param vector ByteVector a transformar.
     * @return ByteVector transformado.
     */
    private byte[] byteVectorToArray(ByteVector vector) {
        byte[] bytes = new byte[vector.length()];
        for (int i = 0; i < vector.length(); i++)
            bytes[i] = vector.get(i);

        return bytes;
    }
}
