package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;

import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;

public class Picture extends Media {
    public Picture(Bitmap bitmap) {
        preview = bitmap;
    }

    @Override
    public void open() {}

    public Bitmap getImage() {
        return preview;
    }

    public void accept(MediaVisitor visitor) {
        visitor.visit(this);
    }
}
