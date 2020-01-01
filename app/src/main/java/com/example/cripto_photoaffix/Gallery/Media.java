package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;

public abstract class Media {
    protected Bitmap preview;

    public abstract void open();

    public Bitmap getPreview() {
        return preview;
    }
}
