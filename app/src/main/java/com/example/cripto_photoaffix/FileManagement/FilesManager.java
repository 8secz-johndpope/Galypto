package com.example.cripto_photoaffix.FileManagement;

import android.content.Context;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Flatbuffers.FlatBufferBuilder;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFileFBS;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class FilesManager {
    private static FilesManager instance;

    private FilesManager() {}

    /**
     * Returns an instance.
     * @return Instance.
     */
    public static FilesManager getInstance() {
        if (instance == null)
            instance = new FilesManager();

        return instance;
    }

    /**
     * Writes in a certain file data.
     * @param path Path where the file to write is stored.
     * @param data Data to store.
     */
    public void writeToFile(String path, String data) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        try {
            OutputStreamWriter writer = new OutputStreamWriter(activity.openFileOutput(path, Context.MODE_PRIVATE));

            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Retorna el contenido de un archivo especificado.
     * @param path Camino al archivo para obtener la informacion.
     * @return Informacion en forma de String.
     */
    public String getFileContent(String path) {
        StringBuilder res = new StringBuilder();
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        try {
            InputStream input = activity.openFileInput(path);

            if (input != null) {
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String read = bufferedReader.readLine();

                while (read != null) {
                    res.append(read);
                    read = bufferedReader.readLine();
                }

                bufferedReader.close();
                reader.close();

                input.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return res.toString();
    }

    /**
     * Determina si un archiv existe o no.
     * @param path Camino del archivo a determinar su existencia.
     * @return True si el archivo existe, False en caso contrario.
     */
    public boolean exists(String path) {
        File f = new File(path);

        return f.exists();
    }

    /**
     * Serializa una lista de archivos encriptados.
     * @param files Archivos encriptados a serializar.
     */
    public void store(List<EncryptedFile> files) {
        SecureRandom random = new SecureRandom();
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        if (!exists(activity.getFilesDir() + "/media"))
            createFolder("media");

        EncryptedFile file;
        int size = files.size();

        for (int i = 0; i < size; i++) {
            file = files.get(i);

            System.out.println("Storing: " + file.getPath() + "/" + file.getFileName());
            String name = random.nextInt() + "";

            while (exists(activity.getFilesDir() + "/media/" + name))
                name = random.nextInt() + "";

            file.setFileName(name);
            file.setPath(activity.getFilesDir() + "/media");

            storeObject(file, activity.getFilesDir() + "/media" ,file.getFileName());
        }
    }

    /**
     * Deserializa los archivos (Media) encriptados.
     * @return Media encriptada.
     */
    public List<EncryptedFile> restoreAllMedia() {
        List<EncryptedFile> files = new ArrayList<EncryptedFile>();
        List<String> names = getMedia();

        int size = names.size();
        String name;

        for (int i = 0; i < size; i++) {
            name = names.get(i);

            files.add(restoreMedia(name));
        }

        names.clear();

        return files;
    }

    /**
     * Restores an EncryptedFile contained in certain path.
     * @param path Path where the EncryptedFile is contained.
     * @return EncryptedFile restored.
     */
    public EncryptedFile restoreMedia(String path) {
        EncryptedFile file = null;

        try {
            ByteBuffer byteBuffer;
            EncryptedFileFBS flatbuffered;
            Deserialazator deserialazator = Deserialazator.getInstance();

            if (!path.endsWith(".mp4") && !path.endsWith(".jpg")) {
                FileInputStream fis = new FileInputStream(path);
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

                byte[] bytes = new byte[fis.available()];
                int read = fis.read(bytes);

                while (read != -1) {
                    byteOutputStream.write(bytes);
                    read = fis.read(bytes);
                }

                byte[] data = byteOutputStream.toByteArray();
                fis.close();
                byteOutputStream.close();

                byteBuffer = ByteBuffer.wrap(data);
                flatbuffered = EncryptedFileFBS.getRootAsEncryptedFileFBS(byteBuffer);

                file = deserialazator.deserialize(flatbuffered);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * Retorna una lista con los caminos a los archivos encriptados.
     * @return Lista con caminos a todos los archivos encriptados.
     */
    public List<String> getMedia() {
        String[] media;
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        File folder = new File(activity.getFilesDir() + "/media");

        if (!folder.exists())
            createFolder("media");


        media = folder.list();

        List<String> res = new ArrayList<String>();

        if (media != null) {
            String s;
            int size = media.length;

            for (int i = 0; i < size; i++) {
                s = media[i];

                res.add(activity.getFilesDir() + "/media/" + s);
            }
        }

        return res;
    }

    /**
     * Retorna una lista con todos los elementos compartidos.
     * @return Lista con camino a todos los elementos compartidos.
     */
    public List<String> getShared() {
        String[] media;
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        File folder = new File(activity.getCacheDir().getPath());

        media = folder.list();

        List<String> res = new ArrayList<String>();

        if (media != null) {
            String s;
            int size = media.length;

            for (int i = 0; i < size; i++) {
                s = media[i];

                res.add(activity.getCacheDir().getPath() + "/" + s);
            }
        }

        return res;
    }

    /**
     * Elimina todos los datos de la aplicacion.
     */
    public void removeEverything() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        File [] files = activity.getFilesDir().listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile())
                files[i].delete();
            else {
                deleteFiles(files[i].listFiles());
                files[i].delete();
            }
        }

        files = activity.getCacheDir().listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile())
                files[i].delete();
            else {
                deleteFiles(files[i].listFiles());
                files[i].delete();
            }
        }
    }

    /**
     * Elimina todos los archivos en un arreglo de archivos.
     * @param files Archivos a eliminar.
     */
    private void deleteFiles(File [] files) {
        int size = 0;

        if (files != null)
            size = files.length;

        File file;

        for (int i = 0; i < size; i++) {
            file = files[i];

            if (!file.isFile())
                deleteFiles(file.listFiles());

            file.delete();
        }
    }

    /**
     * Elimina un archivo en un camino determinado.
     * @param path Camino del archivo a eliminar.
     */
    public void removeFile(String path) {
        File file = new File(path);

        if (file.exists()) {
            if (!file.isFile())
                deleteFiles(file.listFiles());

            file.delete();
        }
    }

    /**
     * Guarda una contraseña (archivo encriptado).
     * @param password Contraseña a guardar.
     */
    public void storePassword(EncryptedFile password) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        password.setPath(activity.getFilesDir().toString());
        storeObject(password, activity.getFilesDir().toString(), password.getFileName());
    }

    /**
     * Copia un archivo a otro destino.
     * @param origin Camino al archivo cual copiar.
     * @param destination Destino donde pegar el archivo.
     * @return Archivo destino.
     */
    public static File copy(String origin, String destination) {
        File res = null;

        try {

            FileInputStream input = new FileInputStream(origin);
            FileOutputStream output = new FileOutputStream(destination);

            byte[] bytes = new byte[input.available()];
            int read = input.read(bytes);

            while (read != -1) {
                output.write(bytes);
                read = input.read(bytes);
            }

            input.close();
            output.flush();
            output.close();

            res = new File(destination);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Restaura la contraseña con camino en name.
     * @param path Nombre de la contraseña.
     * @return Archivo encriptado con la contraseña.
     */
    public EncryptedFile restoreFile(String path) {
        EncryptedFile file = null;
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        try {
            File f = new File(activity.getFilesDir() + "/" + path);
            FileInputStream fis = new FileInputStream(f);

            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            EncryptedFileFBS fbs = EncryptedFileFBS.getRootAsEncryptedFileFBS(byteBuffer);
            Deserialazator deserialazator = Deserialazator.getInstance();

            file = deserialazator.deserialize(fbs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * Serializa un archivo encriptado en un camino especificado y con un nombre especificado.
     * @param file Archivo a serializar.
     * @param path Camino en donde guardar.
     * @param name Nombre del archivo a ser guardado.
     */
    private void storeObject(EncryptedFile file, String path, String name) {
        FlatBufferBuilder builder = file.serialize();

        byte[] bytes = builder.sizedByteArray();

        try {

            FileOutputStream outputStream = new FileOutputStream(path + '/' + name);

            outputStream.write(bytes);

            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una carpeta con un determinado nombre.
     * @param name Nombre de la carpeta a crear.
     */
    private void createFolder(String name) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        File path = new File(activity.getFilesDir(), name);

        if (!path.exists())
            path.mkdirs();
    }
}
