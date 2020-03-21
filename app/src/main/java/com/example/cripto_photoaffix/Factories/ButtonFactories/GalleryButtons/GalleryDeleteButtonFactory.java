package com.example.cripto_photoaffix.Factories.ButtonFactories.GalleryButtons;

import android.view.View;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.DeleteCommand;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import java.util.Map;

public class GalleryDeleteButtonFactory extends GalleryButtonFactory {
    public GalleryDeleteButtonFactory(View layout, int layoutID, Map<Media, MyImageButton> mediaButtons) {
        super(layout, layoutID, mediaButtons);
    }

    protected Command command() {
        return new DeleteCommand();
    }
}
