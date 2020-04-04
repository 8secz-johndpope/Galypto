package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.PreviewChache;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;

public class Video extends Media {

    public Video(String path) {
        this.path = path;
    }

    public void accept(MediaVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public File store(String sharingPath) {
        File path = new File(sharingPath);

        if (!path.exists())
            path.mkdir();

        File toShare = new File(sharingPath, filename + ".mp4");

        for (int i = 0; toShare.exists(); i++)
            toShare = new File(sharingPath, filename + i + ".mp4");

        return FilesManager.copy(getFullPath(), toShare.getPath());
    }

    public String getFullPath() {
        return super.getFullPath() + ".mp4";
    }

    protected void findPreview() {
        Bitmap preview = ThumbnailUtils.createVideoThumbnail(getFullPath(),
                                                             MediaStore.Video.Thumbnails.MINI_KIND);

        PreviewChache.getInstance().addPreview(this, preview);
    }
}
