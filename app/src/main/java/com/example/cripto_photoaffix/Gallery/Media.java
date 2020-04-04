package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;

public abstract class Media {
    protected Bitmap preview;
    protected String path, filename;
    protected boolean selected;

    /**
     * Vista previa de la "Media".
     * @return Bitmap con la vista previa.
     */
    public Bitmap getPreview() {
        return preview;
    }

    /**
     * Accept del visitor.
     * @param visitor Visitor que desea visitar.
     */
    public abstract void accept(MediaVisitor visitor);

    /**
     * Retorna el camino donde esta guardada la media.
     * @return Camino donde esta guardada.
     */
    public String getPath() {
        return path;
    }

    /**
     * Retorna el nombre del archivo.
     * @return Nombre del archivo.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Retorna el camino completo del archivo.
     * @return Camino completo del archivo.
     */
    public String getFullPath() {
        return path + filename;
    }

    /**
     * Cambia el camino del archivo.
     * @param path Camino nuevo del archivo.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Cambia el nombre del archivo.
     * @param name
     */
    public void setFilename(String name) {
        filename = name;

        if (!path.equals(""))
            findPreview();
    }

    /**
     * Guarda el archivo en cierta locacion.
     * @param path Camino a guardar el archivo.
     * @return Archivo donde fue guardado.
     */
    public abstract File store(String path);

    /**
     * Tells whether the media is selected or not.
     * @return true if selected or false if not selected.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Selects the media.
     * @return final result.
     */
    public boolean select() {
        selected = true;
        return selected;
    }

    /**
     * Deselects the media.
     * @return final result.
     */
    public boolean deselect() {
        selected = false;
        return selected;
    }

    protected abstract void findPreview();
}
