package com.example.cripto_photoaffix;

import android.graphics.Bitmap;
import androidx.appcompat.widget.AppCompatImageButton;
import com.example.cripto_photoaffix.Activities.MyActivity;

public class MyImageButton extends AppCompatImageButton {
    private Bitmap bitmap;

    public MyImageButton(Bitmap bitmap, MyActivity activity) {
        super(activity);
        this.bitmap = bitmap;
        setImageBitmap(bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
