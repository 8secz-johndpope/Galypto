package com.example.cripto_photoaffix;

import com.example.cripto_photoaffix.Gallery.Gallery;

public class GalleryTransferer {
    private static final GalleryTransferer instance = new GalleryTransferer();
    private Gallery gallery;

    private GalleryTransferer() {
        gallery = null;
    }

    /**
     * Retorna instancia de la clase ya que es un Singleton.
     * @return Instancia de la clase.
     */
    public static GalleryTransferer getInstance() {
        return instance;
    }

    /**
     * Retorna la galeria siendo utilizada.
     * @return Galeria siendo utilizada.
     */
    public Gallery getGallery() {
        return gallery;
    }

    /**
     * Guarda la galeria a utilizar.
     * @param gallery Galeria a guardar y utilizar.
     */
    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
