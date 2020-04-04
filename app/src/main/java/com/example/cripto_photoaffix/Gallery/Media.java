package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;
import com.example.cripto_photoaffix.PreviewChache;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;

public abstract class Media {
    protected String path, filename;
    protected boolean selected;

    /**
     * Returns the media's preview.
     * @return Preview's bitmap.
     */
    public Bitmap getPreview() {
        if (PreviewChache.getInstance().getPreview(this) == null)
            findPreview();

        return PreviewChache.getInstance().getPreview(this);
    }

    /**
     * @param visitor Visitor visiting.
     */
    public abstract void accept(MediaVisitor visitor);

    /**
     * Returns media's path.
     * @return Path where the media is stored.
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the file name.
     * @return Filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the file's full path. This is path/filename.
     * @return Full path.
     */
    public String getFullPath() {
        return path + filename;
    }

    /**
     * Changes the file's path.
     * @param path New path.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Changes the filename.
     * @param name filename to set.
     */
    public void setFilename(String name) {
        filename = name;

        if (!path.equals(""))
            findPreview();
    }

    /**
     * Stores the file in certain location.
     * @param path Path where to store the file.
     * @return File where it has been stored.
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

    /**
     * Stores the preview in the LruCache.
     */
    protected abstract void findPreview();
}
