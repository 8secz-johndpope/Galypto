package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons;

import android.view.View;
import android.widget.ImageView;

public class RotateLeftButtonFactory extends RotateImageButtonFactory {
    public RotateLeftButtonFactory(View layout, int layoutID, ImageView view) {
        super(layout, layoutID, view);
    }

    @Override
    protected float degrees() {
        float rotation = view.getRotation();

        return rotation - 90;
    }
}
