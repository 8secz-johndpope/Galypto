package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import com.example.cripto_photoaffix.Activities.MyActivity;

public abstract class ButtonFactory {
    protected MyActivity activity;

    protected ButtonFactory(MyActivity activity) {
        this.activity = activity;
    }

    public abstract ImageButton create();

    protected Drawable resizeDrawable(Drawable drawable) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        int height = (int)(metrics.heightPixels * 0.01);
        int width = (int)(metrics.widthPixels * 0.02);
        b = Bitmap.createScaledBitmap(b, height, width, false);

        BitmapDrawable res = new BitmapDrawable(activity.getResources(), b);
        return res;
    }
}
