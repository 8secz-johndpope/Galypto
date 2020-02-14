package com.example.cripto_photoaffix.Gallery;

import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;

public class Video extends Media {

    public Video(String path) {
        this.path = path;
        findPreview();
    }

    /**
     * Encuentra una vista previa para el video.
     */
    private void findPreview() {
        preview = ThumbnailUtils.createVideoThumbnail(path + ".mp4", MediaStore.Video.Thumbnails.MICRO_KIND);
    }

    public void accept(MediaVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public File share(String sharingPath) {
        File toShare = new File(sharingPath, filename + ".mp4");

        for (int i = 0; toShare.exists(); i++)
            toShare = new File(sharingPath, filename + i + ".mp4");

        return FilesManager.copy(getFullPath(), toShare.getPath());
    }

    @Override
    public File store(String path) {
        File toSave = new File(path, filename + ".mp4");

        for (int i = 0; toSave.exists(); i++)
            toSave = new File(path, filename + i + ".mp4");

        return FilesManager.copy(getFullPath(), toSave.getPath());
    }

    public String getFullPath() {
        return super.getFullPath() + ".mp4";
    }
}
