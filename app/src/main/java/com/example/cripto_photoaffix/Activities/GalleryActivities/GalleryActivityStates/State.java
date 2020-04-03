package com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates;

import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import java.util.List;

public interface State {
    /**
     * Cuando el usuario toca una imagen, se hace esta tarea.
     * @param button Boton de imagen o video que el usuario toco.
     */
    public void touch(MyImageButton button);

    /**
     * Si el usuario presiona para ir para atras se realiza esta tarea.
     */
    public void back();

    /**
     * Si el usuario hace un "long press" se realiza esta tarea.
     * @param button Boton de la imagen on video que el usuario presiono por un tiempo "largo".
     */
    public void onLongPress(MyImageButton button);

    /**
     * Si se frena la actividad actual, se realiza esta tarea.
     */
    public void onPause();

    /**
     * Si se reanuda, se realiza esta tarea.
     */
    public void onResume();

    /**
     * Si se reanuda, se realiza esta tarea.
     */
    public void onRestart();

    /**
     * Retorna el siguiente estado.
     * @return Siguiente estado.
     */
    public State getNextState();

    /**
     * Returns the elements previously selected.
     * @return List of media with elements selected.
     */
    public List<Media> getSelected();
}
