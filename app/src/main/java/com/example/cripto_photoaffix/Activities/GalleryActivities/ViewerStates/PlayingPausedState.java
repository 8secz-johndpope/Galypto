package com.example.cripto_photoaffix.Activities.GalleryActivities.ViewerStates;

import android.view.View;
import android.widget.ImageButton;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import java.util.ArrayList;
import java.util.List;

public abstract class PlayingPausedState implements State {
    private List<ImageButton> buttons;
    private boolean buttonsHidden;
    protected ActivityVisitor visitor;
    protected State nextState;

    protected PlayingPausedState() {
        buttons = new ArrayList<ImageButton>();
        buttonsHidden = false;
    }

    protected PlayingPausedState(State nextState) {
        buttons = new ArrayList<ImageButton>();
        buttonsHidden = false;
        this.nextState = nextState;
    }

    @Override
    public void touchScreen() {
        int size = buttons.size();
        ImageButton button;
        int visibility;

        if (buttonsHidden)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        for (int i = 0; i < size; i++) {
            button = buttons.get(i);
            button.setVisibility(visibility);
            i++;
        }
    }

    @Override
    public void setButtons(List<ImageButton> buttons) {
        this.buttons = buttons;
    }

    @Override
    public State getNextState() {
        nextState.setButtons(buttons);
        return nextState;
    }
}
