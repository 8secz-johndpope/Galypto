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
    private boolean goToLogin;

    public Opener() {
        openedImage = false;
        goToLogin = false;
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
    public void onLongPress() {
        ActivityVisitor visitor = new OpenerLongPressVisitor();
        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        activity.accept(visitor);
    }

    @Override
    public void onPause() {
        if (!openedImage) {
            Command command = new RemoveDecryptedMediaCommand();
            command.execute();

            goToLogin = true;
        }
    }

    @Override
    public void onRestart() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        if (!openedImage || goToLogin)
            goToLogin();
        else {
            activity.refresh();
            openedImage = false;
        }
    }

    @Override
    public void onResume() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        if (goToLogin)
            goToLogin();
        else
            activity.refresh();
    }

    @Override
    public State getNextState() {
        return this;
    }

    private void goToLogin() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        Command command = new RemoveSharedCommand();
        command.execute();

        IntentFactory factory = new LoginIntentFactory();
        activity.startActivity(factory.create());
        activity.finish();
    }
}
