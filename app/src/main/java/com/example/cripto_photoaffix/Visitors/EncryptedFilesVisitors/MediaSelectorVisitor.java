package com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Gallery.Picture;
import com.example.cripto_photoaffix.Gallery.Video;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPassword;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedVideo;

public class MediaSelectorVisitor implements EncryptedFileVisitor {
    private String password;

    public MediaSelectorVisitor(String password) {
        this.password = password;
    }

    public Media visit(EncryptedPicture picture) {
        System.out.println("Picture: " + picture.getPath() + "/" + picture.getFileName());
        Media pic = new Picture(picture.decrypt(password));

        if (picture.getPath().endsWith("/"))
            pic.setPath(picture.getPath());
        else
            pic.setPath(picture.getPath() + "/");

        pic.setFilename(picture.getFileName());

        return pic;
    }

    public Media visit(EncryptedVideo video) {
        System.out.println("Video: " + video.getPath() + "/" + video.getFileName());

        Media vid = new Video(video.decrypt(password));

        if (video.getPath().endsWith("/"))
            vid.setPath(video.getPath());
        else
            vid.setPath(video.getPath() + "/");

        vid.setFilename(video.getFileName());

        return vid;
    }

    public Media visit(EncryptedPassword password) {
        return null;
    }
}
