package com.example.cripto_photoaffix;

import android.graphics.Bitmap;
import androidx.appcompat.widget.AppCompatImageButton;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Gallery.Media;

public class MyImageButton extends AppCompatImageButton {
    private Media media;

    public MyImageButton(Media media, MyActivity activity) {
        super(activity);
        this.media = media;
        setImageBitmap(media.getPreview());
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }

    public Bitmap getPreview() {
        return media.getPreview();
    }
}
