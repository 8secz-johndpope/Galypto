package com.example.cripto_photoaffix;

import android.graphics.Bitmap;
import java.util.LinkedList;
import java.util.List;

public class Gallery {
    private List<Bitmap> pictures;

    public Gallery(List<Bitmap> bitmaps) {
        pictures = new LinkedList<Bitmap>();

        for (Bitmap bitmap: bitmaps) {
            pictures.add(bitmap);
        }
    }

    public Gallery() {
        pictures = new LinkedList<Bitmap>();
    }

    public void addPicture(Bitmap picture) {
        pictures.add(picture);
    }

    public List<Bitmap> getPictures() {
        return pictures;
    }
}
