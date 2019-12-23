package com.example.cripto_photoaffix.FileManagement;

import android.content.Context;
import com.example.cripto_photoaffix.Activities.MyActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TextFilesManager {
    private MyActivity activity;

    public TextFilesManager(MyActivity c) {
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

    public void writeToFile(String name, String data) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(activity.openFileOutput(name, Context.MODE_PRIVATE));
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
        boolean does = false;
        String [] fileNames = activity.fileList();
        int i = 0;

        while (i < fileNames.length && !does) {
            does = fileNames[i].equals(name);
            System.out.println(does + " " + name);
            i++;
        }

        return does;
    }
}
