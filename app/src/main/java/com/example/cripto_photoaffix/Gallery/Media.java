package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;

import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;

public abstract class Media {
    protected Bitmap preview;

    public abstract void open();

    public Bitmap getPreview() {
        return preview;
    }

    public abstract void accept(MediaVisitor visitor);
}
