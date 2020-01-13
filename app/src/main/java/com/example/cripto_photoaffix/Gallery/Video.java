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

    private void findPreview() {
        preview = ThumbnailUtils.createVideoThumbnail(path + ".mp4", MediaStore.Video.Thumbnails.MICRO_KIND);
    }

    public void accept(MediaVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public File share(String sharingPath) {
        File toShare = new File(sharingPath + "/video_to_share.mp4");

        for (int i = 0; toShare.exists(); i++)
            toShare = new File(sharingPath + "/video_to_share_" + i + ".mp4");

        return FilesManager.copy(path + ".mp4", toShare.getPath());
    }

    @Override
    public File store(String path) {
        File toSave = new File(path + "/video_to_save.mp4");

        for (int i = 0; toSave.exists(); i++)
            toSave = new File(path + "/video_to_share_" + i + ".mp4");

        return FilesManager.copy(this.path + ".mp4", toSave.getPath());
    }

    public String getFullPath() {
        return super.getFullPath() + ".jpg";
    }
}
