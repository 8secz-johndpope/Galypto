package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;

public abstract class Media {
    protected Bitmap preview;
    protected String path, filename;

    public Bitmap getPreview() {
        return preview;
    }

    public abstract void accept(MediaVisitor visitor);

    public String getPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }

    public String getFullPath() {
        return path + filename;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFilename(String name) {
        filename = name;
    }

    public abstract File share(String sharingPath);

    public abstract File store(String path);
}
