package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons;

import android.view.View;
import android.widget.ImageView;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;

public abstract class RotateImageButtonFactory extends LayoutButtonFactory {
    protected ImageView view;

    public RotateImageButtonFactory(View layout, int layoutID, ImageView view) {
        super(layout, layoutID);

        this.view = view;
    }

    /**
     * Rota la imagen en la vista "view".
     */
    protected void rotate() {
        view.setRotation(degrees());
    }

    /**
     * Retorna los grados a rotar la vista.
     * @return Grados a rotar.
     */
    protected abstract float degrees();

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
