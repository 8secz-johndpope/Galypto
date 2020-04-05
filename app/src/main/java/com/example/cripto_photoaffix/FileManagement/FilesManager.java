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
     * Returns the content of certain file.
     * @param path Path to file with the data.
     * @return Data in string.
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
     * Determines whether a file exists or not.
     * @param path Path of file.
     * @return True if exists, False if does not exists.
     */
    public boolean exists(String path) {
        File f = new File(path);

        return f.exists();
    }

    /**
     * Serializes a list of EncryptedFile.
     * @param files EncryptedFiles to serialize.
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
     * Deserializes all the EncryptedMedia.
     * @return List of Media encrypted.
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
     * Returns a list with the path of all encrypted media.
     * @return List with the path of all encrypted media.
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
     * Returns a list with the path of all shared elements.
     * @return List with the path of all shared elements.
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
     * Removes all data in the app.
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
     * Removes all files contained in the array.
     * @param files Files to delete.
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
     * Deletes a file in certain patth.
     * @param path File path to remove.
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
     * Stores a passowrd (archivo encriptado).
     * @param password Password to store.
     */
    public void storePassword(EncryptedFile password) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        password.setPath(activity.getFilesDir().toString());
        storeObject(password, activity.getFilesDir().toString(), password.getFileName());
    }

    /**
     * Copies a file to another destination.
     * @param origin Path where copy from.
     * @param destination Path where copy to.
     * @return File in destination.
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
     * Restores a password in certain path.
     * @param path Password path.
     * @return EncryptedFile with password.
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
     * Serializes an EncryptedFile in the give path with a given name.
     * @param file File to serialize.
     * @param path Path where to store the file.
     * @param name Name to give the file.
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
     * Creates a folder.
     * @param name Name of the folder.
     */
    private void createFolder(String name) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        File path = new File(activity.getFilesDir(), name);

        if (!path.exists())
            path.mkdirs();
    }
}
