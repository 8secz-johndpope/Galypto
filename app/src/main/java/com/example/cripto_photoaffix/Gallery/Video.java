package com.example.cripto_photoaffix.Gallery;


import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;

public class Video extends Media {

    public Video(String path) {
        this.path = path;
        findPreview();
    }

    @Override
    public void open() {}

    private void findPreview() {
        preview = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
    }

    public void accept(MediaVisitor visitor) {
        visitor.visit(this);
    }
}
