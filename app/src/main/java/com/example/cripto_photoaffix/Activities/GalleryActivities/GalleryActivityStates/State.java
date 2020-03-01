package com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates;

import com.example.cripto_photoaffix.MyImageButton;

public interface State {
    /**
     * Cuando el usuario toca una imagen, se hace esta tarea.
     * @param button Imagen que el usuario toco.
     */
    public void touch(MyImageButton button);

    /**
     * Si el usuario presiona para ir para atras se realiza esta tarea.
     */
    public void back();

    /**
     * Si el usuario hace un "long press" se realiza esta tarea.
     */
    public void onLongPress();

    /**
     * Si se frena la actividad actual, se realiza esta tarea.
     */
    public void onPause();

    /**
     * Si se frena la actividad actual, se realiza esta tarea.
     */
    public void onStop();

    /**
     * Si se sale de la actividad actual, se realiza esta tarea.
     */
    public void onRestart();

    /**
     * Retorna el siguiente estado.
     * @return Siguiente estado.
     */
    public State getNextState();
}
