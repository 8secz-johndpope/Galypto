package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.view.View;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MediaTransferer;

public class ButtonListener implements View.OnClickListener {
    private Command command;

    public ButtonListener(Command command) {
        this.command = command;
    }

    @Override
    public void onClick(View v) {
        Media media = MediaTransferer.getInstance().getMedia();

        command.addMedia(media);
        command.execute();
    }
}
