package com.example.cripto_photoaffix.Factories.ButtonFactories.GalleryButtons;

import android.util.ArrayMap;
import android.view.View;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.GalleryButtonsVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GalleryButtonFactory extends LayoutButtonFactory {
    private Map<Media, MyImageButton> mediaButtons;

    protected GalleryButtonFactory(View layout, int layoutID, Map<Media, MyImageButton> mediaButtons) {
        super(layout, layoutID);

        this.mediaButtons = mediaButtons;
    }

    protected abstract Command command();

    protected View.OnClickListener listener() {
        System.out.println("Returning listener...");
        return new MultipleActionButtonListener(command());
    }

    private Map<MyImageButton, Media> invertMap() {
        Map<MyImageButton, Media> res = new ArrayMap<MyImageButton, Media>();

        for (Media media: mediaButtons.keySet())
            res.put(mediaButtons.get(media), media);

        return res;
    }

    /**
     * ButtonListener de los botones flotantes, cada uno tiene su propio comando.
     */
    private class MultipleActionButtonListener implements View.OnClickListener {
        private Command task;
        private ActivityVisitor visitor;

        private MultipleActionButtonListener(Command task) {
            System.out.println("Created " + this + " with command " + task);
            this.task = task;

            visitor = new GalleryButtonsVisitor();
        }

        @Override
        public void onClick(View v) {
            System.out.println("Cliked like a fucker");
            Map<MyImageButton, Media> aux = invertMap();

            List<MyImageButton> galleryMedia = new ArrayList<MyImageButton>(aux.keySet());

            int size = galleryMedia.size();

            Media media;
            MyImageButton button;

            for (int i = 0; i < size; i++) {
                button = galleryMedia.get(i);
                media = aux.get(button);

                if (button.isSelected())
                    task.addMedia(media);

                button.setSelected(false);
                button.setAlpha(1f);
            }

            task.execute();

            ActivityTransferer.getInstance().getActivity().accept(visitor);
        }
    }
}