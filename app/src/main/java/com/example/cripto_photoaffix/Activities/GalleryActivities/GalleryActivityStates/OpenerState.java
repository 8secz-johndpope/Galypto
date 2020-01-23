package com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.RemoveDecryptedMediaCommand;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.LoginIntentFactory;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaOpenerVisitor;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;

public class OpenerState implements State {
    private boolean openedImage;

    public OpenerState() {
        openedImage = false;
    }

    @Override
    public void touch(MyImageButton button) {
        MediaVisitor visitor = new MediaOpenerVisitor();
        Media buttonMedia = button.getMedia();
        buttonMedia.accept(visitor);
        openedImage = true;
    }

    @Override
    public void back() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        activity.onBackPressed();
    }

    @Override
    public void onPause() {
        if (!openedImage) {
            Command removeDecryptedVideos = new RemoveDecryptedMediaCommand();
            removeDecryptedVideos.execute();
        }
    }

    @Override
    public void onRestart() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        if (!openedImage) {
            IntentFactory factory = new LoginIntentFactory();
            activity.startActivity(factory.create());
            activity.finish();
        }
        else {
            activity.refresh();
            openedImage = false;
        }
    }

    @Override
    public State getNextState() {
        return this;
    }
}
