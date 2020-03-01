package com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.RemoveDecryptedMediaCommand;
import com.example.cripto_photoaffix.Commands.RemoveSharedCommand;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.LoginIntentFactory;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.SelectorBackVisitor;

public class Selector implements State {
    private int cantSelected;

    public Selector() {
        cantSelected = 1;
    }

    @Override
    public void touch(MyImageButton button) {
        if (button.isSelected()) {
            button.setSelected(false);
            button.setAlpha(1f);
            cantSelected--;

            if (cantSelected == 0)
                back();
        }
        else {
            button.setSelected(true);
            button.setAlpha(0.5f);
            cantSelected++;
        }
    }

    @Override
    public void back() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        ActivityVisitor visitor = new SelectorBackVisitor(this);
        activity.accept(visitor);
    }

    @Override
    public void onLongPress() {
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
    public void onStop() {}

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
