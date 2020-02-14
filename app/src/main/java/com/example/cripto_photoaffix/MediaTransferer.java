package com.example.cripto_photoaffix;

import com.example.cripto_photoaffix.Gallery.Media;

/**
 * Esta clase es utilizada para transferir la Media a utilizar ya sea en algun comando o al abrir
 * alguna imagen.
 */
public class MediaTransferer {
    private static final MediaTransferer instance = new MediaTransferer();
    private Media media;

    private MediaTransferer() {
        media = null;
    }

    /**
     * Retorna instancia de la clase ya que es un Singleton.
     * @return Instancia de la clase.
     */
    public static MediaTransferer getInstance() {
        return instance;
    }

    /**
     * Retorna la media a transferir.
     * @return Media a transferir.
     */
    public Media getMedia() {
        return media;
    }

    /**
     * Guarda la media a transferir.
     * @param media Media a guardar para ser transferida.
     */
    public void setMedia(Media media) {
        this.media = media;
    }
}
