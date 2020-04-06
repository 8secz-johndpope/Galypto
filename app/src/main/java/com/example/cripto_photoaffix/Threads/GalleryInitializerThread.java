package com.example.cripto_photoaffix.Threads;

import android.net.Uri;
import android.view.WindowManager;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.GalleryTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.GalleryIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Gallery.Gallery;
import java.util.List;

/**
 * Este hilo abre la galeria. Es usado para poder poner la pantalla de carga en la pantalla
 * principal, debido a que este proceso es pesado, si no se usase este hilo la pantalla de carga
 * cargaria solamente una vez que se termina de desencriptar toda la galeria, dando una mala
 * experiencia ya que la aplicacion pareceria estar congelada y no se veria la carga porque se
 * abriria inmediatamente la galeria.
 */
public class GalleryInitializerThread extends Thread {
    private List<Uri> toEncrypt;
    private String password;

    public GalleryInitializerThread(List<Uri> toEncrypt, String password) {
        this.toEncrypt = toEncrypt;
        this.password = password;
    }

    public void run() {
        MyActivity currentActivity = ActivityTransferer.getInstance().getActivity();

        currentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Gallery gallery;

        if (toEncrypt.isEmpty())
            gallery = new Gallery(password);
        else
            gallery = new Gallery(password, toEncrypt);

        GalleryTransferer transferer = GalleryTransferer.getInstance();
        transferer.setGallery(gallery);

        IntentFactory factory = new GalleryIntentFactory();
        currentActivity.startActivity(factory.create());

        currentActivity.finish();
    }
}
