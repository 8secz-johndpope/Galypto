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

    protected FilesManager() {}

    public static FilesManager getInstance() {
        if (instance == null)
            instance = new FilesManager();

        return instance;
    }

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

    public boolean exists(String path) {
        File f = new File(path);

        return f.exists();
    }

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

    public List<EncryptedFile> restoreMedia() {
        List<EncryptedFile> files = new ArrayList<EncryptedFile>();
        List<String> names = getMedia();

        try {
            EncryptedFile file;
            ByteBuffer byteBuffer;
            EncryptedFileFBS flatbuffered;
            Deserialazator deserialazator = Deserialazator.getInstance();

            int size = names.size();
            String name;

            for (int i = 0; i < size; i++) {
                name = names.get(i);

                if (!name.endsWith(".mp4") && !name.endsWith(".jpg")) {
                    FileInputStream fis = new FileInputStream(name);
                    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

                    byte[] bytes = new byte[4096];
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

                    files.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        names.clear();

        return files;
    }

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

                res.add(activity.getFilesDir() + "/media/" + s);
            }
        }

        return res;
    }

    public void removeEverything() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        File folder = new File(activity.getFilesDir() + "/media");

        if (folder.exists()) {
            deleteFiles(folder.listFiles());

            folder.delete();
        }

        folder = new File(activity.getFilesDir() + "/passwords");

        if (folder.exists()) {
            deleteFiles(folder.listFiles());

            folder.delete();
        }

        File password = new File(activity.getFilesDir() + "/passcodePassword");

        if (password.exists())
            password.delete();

        password = new File(activity.getFilesDir() + "/passcodeFinalPassword");

        if (password.exists())
            password.delete();

        password = new File(activity.getFilesDir() + "/fingerprintFinalPassword");

        if (password.exists())
            password.delete();
    }

    private void deleteFiles(File [] files) {
        int size = 0;

        if (files != null)
            size = files.length;

        File file;

        for (int i = 0; i < size; i++) {
            file = files[i];
            file.delete();
        }
    }

    public void removeFile(String path) {
        File file = new File(path);

        if (file.exists())
            file.delete();
    }

    public void storePassword(EncryptedFile password) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        password.setPath(activity.getFilesDir().toString());
        storeObject(password, activity.getFilesDir().toString(), password.getFileName());
    }

    public static File copy(String origin, String destination) {
        File res = null;

        try {

            FileInputStream input = new FileInputStream(origin);
            FileOutputStream output = new FileOutputStream(destination);

            byte[] bytes = new byte[4096];
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

    public EncryptedFile restorePassword(String name) {
        EncryptedFile file = null;
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        try {
            File f = new File(activity.getFilesDir() + "/" + name);
            FileInputStream fis = new FileInputStream(f);

            byte[] data = new byte[(int) f.length()];
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

    private void createFolder(String name) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        File path = new File(activity.getFilesDir(), name);

        if (!path.exists())
            path.mkdirs();
    }
}
