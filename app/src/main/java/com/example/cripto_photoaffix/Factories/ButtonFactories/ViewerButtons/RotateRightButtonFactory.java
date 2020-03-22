package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons;

import android.view.View;
import android.widget.ImageView;

public class RotateRightButtonFactory extends RotateImageButtonFactory {
    public RotateRightButtonFactory(View layout, int layoutID, ImageView view) {
        super(layout, layoutID, view);
    }

    @Override
    protected float degrees() {
        float rotation = view.getRotation();

        return rotation + 90;
    }
}
