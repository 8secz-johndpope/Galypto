package com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates;

import com.example.cripto_photoaffix.MyImageButton;

public interface State {
    public void touch(MyImageButton button);
    public void back();
    public void onLongPress();
    public void onPause();
    public void onRestart();
    public State getNextState();
}
