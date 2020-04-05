package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons;

import android.view.View;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

public abstract class RotateImageButtonFactory extends LayoutButtonFactory {
    protected ActivityVisitor visitor;

    public RotateImageButtonFactory(View layout, int layoutID) {
        super(layout, layoutID);
    }

    /**
     * Rotates the image.
     */
    protected void rotate() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        activity.accept(visitor);
    }

    protected View.OnClickListener listener() {
        return new RotatorListener();
    }

    private class RotatorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            rotate();
        }
    }
}
