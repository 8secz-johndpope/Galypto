package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;

public abstract class ButtonFactory {
    public abstract ImageButton create();

    protected Drawable resizeDrawable(Drawable drawable) {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Bitmap originalBitmap = ((BitmapDrawable)drawable).getBitmap();

        int newHeight = (int)(metrics.heightPixels * 0.01);
        int newWidth = (int)(metrics.widthPixels * 0.02);

        float scaledHeight = (float)newHeight/originalBitmap.getHeight();
        float scaledWidth = (float)newWidth/originalBitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.postScale(scaledWidth, scaledHeight);

        Bitmap bitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(),
                 matrix, true);

        return new BitmapDrawable(activity.getResources(), bitmap);
    }
}
