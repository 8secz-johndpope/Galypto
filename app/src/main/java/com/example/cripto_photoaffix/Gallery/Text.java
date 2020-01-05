package com.example.cripto_photoaffix.Gallery;


import android.graphics.Bitmap;

import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Text extends Media {
    public void accept(MediaVisitor visitor) {}

    @Override
    public File share(String sharingPath) {
        File file = new File(sharingPath, "sharing.png");

        int i = 0;

        while (file.exists()) {
            file = new File(sharingPath, "sharing" + i + ".mp4");
            i++;
        }


        return file;
    }
}
