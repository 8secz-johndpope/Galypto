package com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.RemoveDecryptedMediaCommand;
import com.example.cripto_photoaffix.Commands.RemoveSharedCommand;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.LoginIntentFactory;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.OpenerLongPressVisitor;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaOpenerVisitor;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;

public class Opener implements State {
    private boolean openedImage;
    private boolean justOpened;

    public Opener() {
        openedImage = false;
        justOpened = true;
    }

    @Override
    public void touch(MyImageButton button) {
        justOpened = false;
        MediaVisitor visitor = new MediaOpenerVisitor();
        Media buttonMedia = button.getMedia();
        buttonMedia.accept(visitor);
        openedImage = true;
    }

    @Override
    public void back() {
        justOpened = false;
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        activity.onBackPressed();
    }

    @Override
    public void onLongPress() {
        justOpened = false;
        ActivityVisitor visitor = new OpenerLongPressVisitor();
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        activity.accept(visitor);
    }

    @Override
    public void onPause() {
        if (!openedImage && !justOpened)
            exit();
    }

    @Override
    public void onStop() {
        if (!openedImage)
            exit();
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

    private void exit() {
        Command command = new RemoveDecryptedMediaCommand();
        command.execute();

        command = new RemoveSharedCommand();
        command.execute();
    }
}
