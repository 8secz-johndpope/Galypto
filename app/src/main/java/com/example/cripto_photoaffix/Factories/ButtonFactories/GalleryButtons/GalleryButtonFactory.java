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
     * Retorna el comando necesario para ejecutar una vez que se toca al boton.
     * @return Comando a ejecutar.
     */
    protected abstract Command command();

    protected View.OnClickListener listener() {
        return new GalleryButtonListener(command());
    }

    /**
     * ButtonListener de los botones flotantes, cada uno tiene su propio comando.
     */
    private class GalleryButtonListener implements View.OnClickListener {
        private ActivityVisitor visitor;

        private GalleryButtonListener(Command task) {
            visitor = new GalleryButtonVisitor(task);
        }

        @Override
        public void onClick(View v) {
            MyActivity activity = ActivityTransferer.getInstance().getActivity();
            activity.accept(visitor);
        }
    }
}
