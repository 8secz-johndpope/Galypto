package com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Gallery.Picture;
import com.example.cripto_photoaffix.Gallery.Text;
import com.example.cripto_photoaffix.Gallery.Video;
import com.example.cripto_photoaffix.Security.EncryptedPassword;
import com.example.cripto_photoaffix.Security.EncryptedPicture;
import com.example.cripto_photoaffix.Security.EncryptedVideo;

public class MediaSelectorVisitor implements EncryptedFileVisitor {
    private String password;

    public MediaSelectorVisitor(String password) {
        this.password = password;
    }

    public Media visit(EncryptedPicture picture) {
        System.out.println("Picture: " + picture.getPath() + "/" + picture.getFileName());
        return new Picture(stringToBitmap(picture.decrypt(password)));
    }

    public Media visit(EncryptedVideo video) {
        System.out.println("Video: " + video.getPath() + "/" + video.getFileName());
        return new Video(video.decrypt(password));
    }

    public Media visit(EncryptedPassword password) {
        return new Text();
    }

    private Bitmap stringToBitmap(String bit) {
        byte[] bytes = Base64.decode(bit, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }
}
