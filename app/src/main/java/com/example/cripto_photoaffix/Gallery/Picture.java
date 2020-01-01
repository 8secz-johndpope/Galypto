package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;

public class Picture extends Media {
    public Picture(Bitmap bitmap) {
        preview = bitmap;
    }

    @Override
    public void open() {}
}
