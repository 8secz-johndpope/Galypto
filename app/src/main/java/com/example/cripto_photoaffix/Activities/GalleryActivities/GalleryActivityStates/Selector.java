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
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.GalleryVisitors.SelectorBackVisitor;

public class Selector implements State {
    private int selected;

    public Selector() {
        selected = 0;
    }

    @Override
    public void touch(MyImageButton button) {
        Media media = button.getMedia();

        if (media.isSelected()) {
            button.setSelected(false);
            button.setAlpha(1f);
            media.deselect();
            selected--;

            if (selected == 0)
                back();
        }
        else {
            media.select();
            button.setSelected(true);
            button.setAlpha(0.5f);
            selected++;
        }
    }

    @Override
    public void back() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        ActivityVisitor visitor = new SelectorBackVisitor(this);
        activity.accept(visitor);
    }

    @Override
    public void onLongPress(MyImageButton button) {
        back();
    }

    @Override
    public void onPause() {
        Command command = new RemoveDecryptedMediaCommand();
        command.execute();

        command = new RemoveSharedCommand();
        command.execute();
    }

    @Override
    public void onResume() {}

    @Override
    public void onRestart() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        IntentFactory factory = new LoginIntentFactory();
        activity.startActivity(factory.create());
        activity.finish();
    }

    @Override
    public State getNextState() {
        return new Opener();
    }
}
