package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons;

import android.view.View;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.RotatorVisitor;

public class RotateRightButtonFactory extends RotateImageButtonFactory {
    public RotateRightButtonFactory(View layout, int layoutID) {
        super(layout, layoutID);

        visitor = new RotatorVisitor(90);
    }
}
