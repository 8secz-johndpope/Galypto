package com.example.cripto_photoaffix;

import com.example.cripto_photoaffix.Gallery.Gallery;

public class GalleryTransferer {
    private static final GalleryTransferer instance = new GalleryTransferer();
    private Gallery gallery;

    private GalleryTransferer() {
        gallery = null;
    }

    public static GalleryTransferer getInstance() {
        return instance;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery d) {
        gallery = d;
    }
}
