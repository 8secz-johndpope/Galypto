package com.example.cripto_photoaffix.Commands;

import android.os.Environment;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Gallery.Media;

public class StoreCommand implements Command {
    protected Media media;

    public StoreCommand(MyActivity activity, Media media) {
        this.media = media;
    }

    @Override
    public void execute() {
        media.store(Environment.getExternalStorageDirectory().toString());
    }
}
