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

    /**
     * Se decripta a si mismo dada una contraseña.
     * @param password Contraseña con la cual desencriptar.
     * @return Informacion desencriptada.
     */
    public String decrypt(String password) {
        MyEncryptor encryptor = MyEncryptor.getInstance();
        return encryptor.decrypt(this, password);
    }

    /**
     * Encripta en si mismo cierta informacion bajo una contraseña. Notese que si habia informacion
     * anteriormente, esta sera perdida.
     * @param data Informacion a encriptar.
     * @param passowrd Contraseña con la cual encriptar la informacion
     */
    public void encrypt(String data, String passowrd) {
        MyEncryptor encryptor = MyEncryptor.getInstance();
        Vector<byte[]> vector = encryptor.encrypt(data, passowrd);

        this.data = vector.get(0);
        this.iv = vector.get(1);
        this.salt = vector.get(2);

        vector.removeAllElements();
    }

    /**
     * Retorna la informacion encriptada.
     * @return Informacion encriptada.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Retorna la "Salt" utilizada.
     * @return "Salt" utilizada.
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * Retorna el vector de inicializacion.
     * @return Vector de inicializacion.
     */
    public byte[] getIV() {
        return iv;
    }

    /**
     * Retorna el nombre del archivo.
     * @return Nombre del archivo.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Retorna el camino al archivo.
     * @return Camino al archivo.
     */
    public String getPath() {
        return path;
    }

    /**
     * Guarda (en memoria) la infomacion indicada. Notese que si habia anteriormente, esta se va a
     * perder.
     * @param data Informacion a guardar.
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * Guarda (en memoria) la "salt" indicada. Notese que si habia anteriormente, esta se va perder.
     * @param salt "Salt" a guardar.
     */
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    /**
     * Guarda (en memoria) el vector de inicializacion. Notese que si habia anteriormente, este se
     * va a perder.
     * @param iv Vector de inicializacion.
     */
    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    /**
     * Guarda (en memoria) el nombre del archivo. Notese que si habia anteriormente, este se va a
     * perder.
     * @param name Nombre del archivo.
     */
    public void setFileName(String name) {
        fileName = name;
    }

    /**
     * Guarda (en memoria) el camino al archivo. Notese que si habia anteriormente, este se va a
     * perder.
     * @param path Camino al archivo.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Accept del visitor que desea visitar.
     * @param visitor Visitor que desea visitar.
     * @return Media si es que desencripto.
     */
    public abstract Media accept(EncryptedFileVisitor visitor);

    /**
     * Retorna un clon del archivo encriptado.
     * @return Clon del archivo encriptado.
     */
    public abstract EncryptedFile clone();

    /**
     * Serializa el archivo encriptado.
     * @return Un constructor de Flatbuffers con el archivo encriptado.
     */
    public abstract FlatBufferBuilder serialize();

    /**
     * Limpia el archivo encriptado de memoria.
     */
    public void clear() {
        data = null;
        salt = null;
        iv = null;
        fileName = null;
        path = null;
    }
}
