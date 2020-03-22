package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons;

import android.view.View;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.RotatorVisitor;

public class RotateLeftButtonFactory extends RotateImageButtonFactory {
    public RotateLeftButtonFactory(View layout, int layoutID) {
        super(layout, layoutID);

        visitor = new RotatorVisitor(-90);
    }
}
