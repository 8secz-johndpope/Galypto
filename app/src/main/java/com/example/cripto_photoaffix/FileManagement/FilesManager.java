package com.example.cripto_photoaffix.FileManagement;

import android.content.Context;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Deserialazator;
import com.example.cripto_photoaffix.Flatbuffers.ByteVector;
import com.example.cripto_photoaffix.Flatbuffers.FlatBufferBuilder;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFileFBS;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPassword;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedVideo;

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
import java.util.LinkedList;
import java.util.List;

public class FilesManager {
    private static FilesManager instance;
    private MyActivity activity;

    protected FilesManager(MyActivity activity) {
        this.activity = activity;
    }

    public static FilesManager getInstance(MyActivity activity) {
        if (instance == null)
            instance = new FilesManager(activity);

        return instance;
    }

    public void createFolder(String name) {
        File path = new File(activity.getFilesDir(), name);

        if (!path.exists())
            path.mkdirs();
    }

    public void writeToFile(String path, String data) {
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

    public boolean exists(String name) {
        File f = new File(name);

        return f.exists();
    }

    public void store(List<EncryptedFile> files) {
        SecureRandom random = new SecureRandom();

        if (!exists(activity.getFilesDir() + "/pictures"))
            createFolder("pictures");

        for (EncryptedFile file: files) {
            String name = random.nextInt() + "";

            while (exists(activity.getFilesDir() + "/pictures/" + name))
                name = random.nextInt() + "";

            file.setFileName(name);
            file.setPath(activity.getFilesDir() + "/pictures");

            storeObject(file, activity.getFilesDir() + "/pictures" ,file.getFileName());
        }
    }

    public List<EncryptedFile> restoreMedia() {
        List<EncryptedFile> files = new LinkedList<EncryptedFile>();
        List<String> names = getMedia();

        try {
            EncryptedFile file;
            ByteBuffer byteBuffer;
            EncryptedFileFBS flatbuffered;
            Deserialazator deserialazator = new Deserialazator();
            for (String name: names) {

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

    public EncryptedFile restorePassword(String name) {
        EncryptedFile file = null;

        try {
            File f = new File(activity.getFilesDir() + "/" + name);
            FileInputStream fis = new FileInputStream(f);

            byte[] data = new byte[(int) f.length()];
            fis.read(data);
            fis.close();

            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            EncryptedFileFBS fbs = EncryptedFileFBS.getRootAsEncryptedFileFBS(byteBuffer);
            Deserialazator deserialazator = new Deserialazator();

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

    public List<String> getMedia() {
        String[] media;

        File folder = new File(activity.getFilesDir() + "/pictures");

        if (!folder.exists())
            createFolder("pictures");


        media = folder.list();

        List<String> res = new LinkedList<String>();

        if (media != null) {
            for (String s : media)
                res.add(activity.getFilesDir() + "/pictures/" + s);
        }

        return res;
    }

    public void removeEverything() {
        File folder = new File(activity.getFilesDir() + "/pictures");

        if (folder.exists()) {
            for (File file : folder.listFiles())
                file.delete();

            folder.delete();
        }

        folder = new File(activity.getFilesDir() + "/passwords");

        if (folder.exists()) {
            for (File file: folder.listFiles())
                file.delete();

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

    public void removeFile(String path) {
        File file = new File(path);

        if (file.exists())
            file.delete();
    }

    public void storePassword(EncryptedFile password) {
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
}
