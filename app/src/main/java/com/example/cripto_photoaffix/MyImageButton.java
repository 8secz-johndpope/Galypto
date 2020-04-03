package com.example.cripto_photoaffix;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageButton;
import com.example.cripto_photoaffix.Gallery.Media;

public class MyImageButton extends AppCompatImageButton {
    private Media media;

    public MyImageButton(Media media) {
        super(ActivityTransferer.getInstance().getActivity());
        this.media = media;

       setMedia(media);
    }

    public MyImageButton(Context context) {
        super(context);

        setStyle();
    }

    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setStyle();
    }

    public MyImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setStyle();
    }

    /**
     * Guarda la media del boton.
     * @param media Media que el boton representa
     */
    public void setMedia(Media media) {
        this.media = media;
        setStyle();

        setImageBitmap(media.getPreview());
    }

    /**
     * Retorna la media que el boton representa.
     * @return Media que el boton representa.
     */
    public Media getMedia() {
        return media;
    }

    public Bitmap getPreview() {
        return media.getPreview();
    }

    private void setStyle() {
        setScaleType(ScaleType.CENTER_CROP);
        setBackgroundResource(R.drawable.roundedimage);
        setCropToPadding(true);
        setPadding(3, 3, 3, 3);
    }
}
