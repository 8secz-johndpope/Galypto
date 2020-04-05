package com.example.cripto_photoaffix.Factories.ButtonFactories.GalleryButtons;

import android.view.View;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.GalleryVisitors.GalleryButtonVisitor;

public abstract class GalleryButtonFactory extends LayoutButtonFactory {
    protected GalleryButtonFactory(View layout, int layoutID) {
        super(layout, layoutID);
    }

    /**
     * Returns the command to execute when the button is pressed.
     * @return Command to execute.
     */
    protected abstract Command command();

    protected View.OnClickListener listener() {
        return new GalleryButtonListener();
    }

    private class GalleryButtonListener implements View.OnClickListener {
        private ActivityVisitor visitor;

        private GalleryButtonListener() {
            visitor = new GalleryButtonVisitor(command());
        }

        @Override
        public void onClick(View v) {
            MyActivity activity = ActivityTransferer.getInstance().getActivity();
            activity.accept(visitor);
        }
    }
}
