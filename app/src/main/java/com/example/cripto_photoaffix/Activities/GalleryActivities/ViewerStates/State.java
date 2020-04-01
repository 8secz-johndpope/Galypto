package com.example.cripto_photoaffix.Activities.GalleryActivities.ViewerStates;

import android.widget.ImageButton;
import java.util.List;

public interface State {
    public void actOnVideo(ImageButton button);
    public void touchScreen();
    public void setButtons(List<ImageButton> buttons);
    public State getNextState();
}
