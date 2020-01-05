package com.example.cripto_photoaffix.Gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.ShareIntentFactory;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Picture extends Media {
    public Picture(Bitmap bitmap) {
        preview = bitmap;
    }

    public Bitmap getImage() {
        return preview;
    }

    public void accept(MediaVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public File share(String sharingPath) {
        File file = new File(sharingPath, "sharing.png");

        int i = 0;

        while (file.exists()) {
            file = new File(sharingPath, "sharing" + i + ".png");
            i++;
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            preview.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
