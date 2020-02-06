package com.example.cripto_photoaffix;

import com.example.cripto_photoaffix.Gallery.Media;

public class MediaTransferer {
    private static final MediaTransferer instance = new MediaTransferer();
    private Media media;

    private MediaTransferer() {
        media = null;
    }

    public static MediaTransferer getInstance() {
        return instance;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media d) {
        media = d;
    }
}
