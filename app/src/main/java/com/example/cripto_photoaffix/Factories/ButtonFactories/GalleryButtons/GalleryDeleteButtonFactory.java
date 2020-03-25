package com.example.cripto_photoaffix.Factories.ButtonFactories.GalleryButtons;

import android.view.View;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.DeleteCommand;

public class GalleryDeleteButtonFactory extends GalleryButtonFactory {
    public GalleryDeleteButtonFactory(View layout, int layoutID) {
        super(layout, layoutID);
    }

    protected Command command() {
        return new DeleteCommand();
    }
}
