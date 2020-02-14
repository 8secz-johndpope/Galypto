package com.example.cripto_photoaffix;

import com.example.cripto_photoaffix.Activities.MyActivity;

/**
 * Esta clase mantiene la actividad actual en la cual se esta trabajando.
 */
public class ActivityTransferer {
    private static final ActivityTransferer instance = new ActivityTransferer();
    private MyActivity activity;

    private ActivityTransferer() {
        activity = null;
    }

    /**
     * Retorna instancia de la clase ya que es un Singleton.
     * @return Instancia de la clase.
     */
    public static ActivityTransferer getInstance() {
        return instance;
    }

    /**
     * Retorna la actividad actual.
     * @return Actividad actual.
     */
    public MyActivity getActivity() {
        return activity;
    }

    /**
     * Guarda como actividad actual la actividad indiacada.
     * @param activity Actividad a guardar como actual.
     */
    public void setActivity(MyActivity activity) {
        this.activity = activity;
    }
}
