package com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates;

import com.example.cripto_photoaffix.MyImageButton;

public interface State {
    /**
     * When the user touches an image, this method is called.
     * @param button Button touched.
     */
    public void touch(MyImageButton button);

    /**
     * When the user presses the back button, this method is called.
     */
    public void back();

    /**
     * If the user long presses an image.
     * @param button Button long pressed.
     */
    public void onLongPress(MyImageButton button);

    /**
     * When the activity is stopped.
     */
    public void onPause();

    /**
     * When the activity is resumed.
     */
    public void onResume();

    /**
     * When the activity is restarted.
     */
    public void onRestart();

    /**
     * Returns the next state to use.
     * @return Next state.
     */
    public State getNextState();
}
