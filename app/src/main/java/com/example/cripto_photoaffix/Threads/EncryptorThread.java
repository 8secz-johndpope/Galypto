package com.example.cripto_photoaffix.Threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Process;
import android.util.Base64;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedVideo;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class EncryptorThread extends Thread {
    private Queue<Uri> toEncrypt;
    private List<EncryptedFile> result;
    private String password;

    public EncryptorThread(Queue<Uri> files, String passcode) {
        super();
        this.toEncrypt = files;
        result = new ArrayList<EncryptedFile>();
        this.password = passcode;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        Uri file;
        EncryptedFile encrypted;

        while (!toEncrypt.isEmpty()) {
            file = toEncrypt.poll();

            encrypted = encryptUri(file);

            result.add(encrypted);

            try {
                Thread.sleep(25);
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retorna una lista con los archivos encriptados.
     * @return Lista con los archivos encriptados.
     */
    public List<EncryptedFile> getEncrypted() {
        return result;
    }

    /**
     * Limpia los elementos del hilo de memoria.
     */
    public void clear() {
        toEncrypt.clear();
        result.clear();
        password = null;
    }

    /**
     * Encripta un URI.
     * @param uri URI a encriptar.
     * @return Archivo encriptado.
     */
    private EncryptedFile encryptUri(Uri uri) {
        EncryptedFile res = null;
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        String type = activity.getContentResolver().getType(uri);

        if (type != null) {
            String data;

            if (type.startsWith("video")) {

                data = getVideoData(uri);

                res = new EncryptedVideo();
                res.encrypt(data, password);

            }
            else {

                data = getImageData(uri);
                res = new EncryptedPicture();
                res.encrypt(data, password);

            }
        }

        return res;
    }

    /**
     * Retorna la informacion de un video en un URI. El metodo es estatico y sincronizado ya que
     * el metodo de "Base64" "encodeToString" es estatico, y al ser utilizado por varios hilos ese
     * metodo provoca errores retornando posiblemente el mismo video para dos videos diferentes.
     * @param uri URI a obtener informacion.
     * @return Informacion del video.
     */
    private static synchronized String getVideoData(Uri uri) {
        String res = null;
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        try {
            InputStream fis = activity.getContentResolver().openInputStream(uri);

            if (fis != null) {
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

                byte[] bytes = new byte[4096];

                int read = fis.read(bytes);

                while (read != -1) {
                    byteOutputStream.write(bytes);
                    read = fis.read(bytes);
                }

                byte[] data = byteOutputStream.toByteArray();

                res = Base64.encodeToString(data, Base64.DEFAULT);

                fis.close();
                byteOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Retorna la informacion de la imagen contenida en el URI.
     * @param uri URI a obtener la informacion de la imagen.
     * @return String con la informacion de la image.
     */
    private String getImageData(Uri uri) {
        Bitmap bitmap = getThumbnail(uri);

        return bitmapToString(bitmap);
    }

    /**
     * Retonra una vista miniatura del URI.
     * @param uri URI a obtener miniatura.
     * @return Miniatura en forma de Bitmap.
     */
    private Bitmap getThumbnail(Uri uri) {
        Bitmap bitmap;
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);

            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            bitmap = null;
        }

        return bitmap;
    }

    /**
     * Transforma un Bitmap en String.
     * @param bitmap Bitmap a transformar en String.
     * @return String del Bitmap.
     */
    private String bitmapToString(Bitmap bitmap) {
        byte [] bytes = null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            bytes = outputStream.toByteArray();

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
