package com.example.cripto_photoaffix.FileManagement;

import android.content.Context;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Security.EncryptedFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

public class FilesManager {
    private MyActivity activity;

    public FilesManager(MyActivity c) {
        activity = c;
    }

    public void createFile(String folder, String filename) {
        File path = new File(activity.getFilesDir(), folder);

        if (!path.exists())
            path.mkdirs();

        File file = new File(path, filename);

        try {

            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

                reader.close();
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

            storeObject(file, activity.getFilesDir() + "/pictures" ,file.getFileName());
        }
    }

    public List<EncryptedFile> restoreMedia() {
        List<EncryptedFile> files = new LinkedList<EncryptedFile>();
        List<String> names = getMedia();

        try {
            EncryptedFile file;
            for (String name: names) {
                FileInputStream inputStream = new FileInputStream(name);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                System.out.println("Restoring " + name);
                file = (EncryptedFile) objectInputStream.readObject();
                files.add(file);

                inputStream.close();
                objectInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

    private void storeObject(Serializable object, String path, String name) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path + '/' + name);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getMedia() {
        String[] media;

        File folder = new File(activity.getFilesDir() + "/pictures");

        if (!folder.exists())
            createFolder("pictures");


        media = folder.list();

        List<String> res = new LinkedList<String>();

        for (String s: media)
            res.add(activity.getFilesDir() + "/pictures/" + s);

        return res;
    }

    public void removeEverything() {
        File folder = new File(activity.getFilesDir() + "/pictures");

        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                file.delete();
            }

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

    public void storePassword(EncryptedFile password) {
        System.out.println();
        if (!exists(activity.getFilesDir().toString() + "/" + password.getFileName()))
            createFile(activity.getFilesDir().toString(), password.getFileName());

        storeObject(password, activity.getFilesDir().toString(), password.getFileName());

        for (File f: activity.getFilesDir().listFiles())
            System.out.println("FILE: " + f.getName());
    }
}
