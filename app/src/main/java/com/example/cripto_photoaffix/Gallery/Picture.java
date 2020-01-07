package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Picture extends Media {
    public Picture(String path) {
        this.path = path;

        preview = BitmapFactory.decodeFile(path);
        preview = Bitmap.createScaledBitmap(preview, (int)(preview.getWidth()*0.5), (int)(preview.getHeight()*0.5), false);
    }

  /*  public Bitmap getImage() {
        return preview;
    }*/

    public void accept(MediaVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public File share(String sharingPath) {
        File path = new File(sharingPath);
        if (!path.exists())
            path.mkdir();

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

    @Override
    public File store(String path) {
        File myDir = new File(path);

        String fname = "Image-" + "storing";
        File file = new File(myDir, fname + ".jpg");

        int i = 0;
        while (file.exists()) {
            file = new File(myDir, fname + i + ".jpg");
            i++;
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            preview.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
}
