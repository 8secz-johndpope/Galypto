package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.Opener;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.State;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Factories.ButtonFactories.ButtonFactory;
import com.example.cripto_photoaffix.Factories.ButtonFactories.GalleryButtons.GalleryDeleteButtonFactory;
import com.example.cripto_photoaffix.Factories.ButtonFactories.GalleryButtons.GalleryShareButtonFactory;
import com.example.cripto_photoaffix.Factories.ButtonFactories.GalleryButtons.GalleryStoreButtonFactory;
import com.example.cripto_photoaffix.GalleryTransferer;
import com.example.cripto_photoaffix.Gallery.Gallery;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import androidx.appcompat.widget.Toolbar;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.cripto_photoaffix.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends MyActivity {

    private Gallery gallery;
    private GridLayout gridLayout;
    private Map<Media, MyImageButton> buttons;
    private List<ImageButton> actionButtons;
    private View actionButtonsView;
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttons = new ArrayMap<Media, MyImageButton>();

        initialize();

        initializeFloatingButtons();
    }

    /**
     * Cambia el estado que se esta utilizando.
     * @param state Nuevo estado.
     */
    public void changeState(State state) {
        this.state = state;
    }

    /**
     * Deselecciona todos los botones previamente seleccionados.
     */
    public void unselectAllButtons() {
        for (MyImageButton button: buttons.values()) {
            button.setSelected(false);
            button.setAlpha(1f);
        }
    }

    /**
     * Retorna el estado actual.
     * @return Estado actual.
     */
    public State getState() {
        return state;
    }

    @Override
    public void refresh() {
        List<Media> galleryMedia = gallery.getMedia();
        View.OnLongClickListener longClickListener = new LongClickListener();
        View.OnClickListener touchListener = new ButtonListener();

        MyImageButton button;
        Media media;
        int size = galleryMedia.size();
        List<Media> toRemove = new ArrayList<Media>(buttons.keySet());

        for (int i = 0; i < size; i++) {
            media = galleryMedia.get(i);

            if (buttons.get(media) == null) {          //Si no hay un boton en la pantalla, lo añado
                button = new MyImageButton(media);

                gridLayout.addView(button, (int)(getScreenWidth() / 3.5), getScreenHeigth() / 7);

                button.setOnClickListener(touchListener);
                button.setOnLongClickListener(longClickListener);

                buttons.put(media, button);
            }
            else                                      //Si hay un boton en la pantalla, lo saco de
                toRemove.remove(media);               //la lista a eliminar botones.
        }

        size = toRemove.size();

        for (int i = 0; i < size; i++) {              //Elimino todos los botones que hay que eliminar.
            media = toRemove.get(i);

            gridLayout.removeView(buttons.get(media));

            buttons.remove(media);
        }
    }

    /**
     * Oculta los botones flotantes (compartir, eliminar y guardar).
     */
    public void hideButtons() {
        int size = actionButtons.size();
        ImageButton b;

        for (int i = 0; i < size; i++) {
            b = actionButtons.get(i);

            b.setVisibility(View.INVISIBLE);
            b.setClickable(false);
        }

        actionButtonsView.setVisibility(View.INVISIBLE);
    }

    /**
     * Muestra todos los botones.
     */
    public void showButtons() {
        int size = actionButtons.size();
        ImageButton b;

        for (int i = 0; i < size; i++) {
            b = actionButtons.get(i);

            b.setVisibility(View.VISIBLE);
            b.setClickable(true);
        }

        actionButtonsView.setVisibility(View.VISIBLE);
    }

    /**
     * Ejecuta en los botones seleccionados una tarea.
     * @param task Tarea a ejecutar.
     */
    public void executeOnSelected(Command task) {
        List<Media> galleryMedia = gallery.getMedia();

        int size = galleryMedia.size();

        Media media;
        MyImageButton button;

        for (int i = 0; i < size; i++) {
            media = galleryMedia.get(i);
            button = buttons.get(media);

            if (button.isSelected())
                task.addMedia(media);

            button.setSelected(false);
            button.setAlpha(1f);
        }

        task.execute();

        refresh();

        hideButtons();

        changeState(state.getNextState());
    }

    /**
     * Si se frena la actividad actual se avisa al estado y se determina la accion a realizar.
     */
    @Override
    public void onPause() {
        super.onPause();

        state.onPause();
    }

    /**
     * Si se reinicia la actividad actual, se avisa al estado actual y se determina la activiad a
     * realizar, tambien se añade a esta actividad como la actual.
     */
    @Override
    public void onRestart() {
        super.onRestart();

        state.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();

        state.onResume();
    }

    public void accept(ActivityVisitor activityVisitor) {
        activityVisitor.visit(this);
    }

    /**
     * Inicializa la grilla de imagenes y el estado.
     */
    private void initialize() {
        GalleryTransferer transferer = GalleryTransferer.getInstance();
        gallery = transferer.getGallery();

        gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(3);

        gridLayout.setRowCount(gallery.getMedia().size()/3 + 1);

        gridLayout.setBackgroundResource(R.drawable.roundedimage);

        refresh();

        state = new Opener();
    }

    /**
     * Inicializa los botones flotantes (compartir, eliminar y borrar).
     */
    private void initializeFloatingButtons() {
        actionButtons = new ArrayList<ImageButton>();

        View layout = findViewById(R.id.constraintLay);

        ButtonFactory factory = new GalleryDeleteButtonFactory(layout, R.id.delete);//, buttons);
        ImageButton button = factory.create();
        actionButtons.add(button);

        factory = new GalleryStoreButtonFactory(layout, R.id.store);//, buttons);
        button = factory.create();
        actionButtons.add(button);

        factory = new GalleryShareButtonFactory(layout, R.id.share);//, buttons);
        button = factory.create();
        actionButtons.add(button);

        actionButtonsView = findViewById(R.id.actionButtonsView);

        hideButtons();
    }

    /**
     * Retorna el ancho de la pantalla.
     * @return Retorna el ancho de la pantalla.
     */
    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    /**
     * Retorna el largo de la pantalla.
     * @return Retorna el largo de la pantalla.
     */
    private int getScreenHeigth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }

    /**
     * ButtonListener de tocado en las imagenes.
     */
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            state.touch((MyImageButton) view);
        }
    }

    /**
     * ButtonListener de tocado largo en las imagenes.
     */
    private class LongClickListener implements View.OnLongClickListener {
        public boolean onLongClick(View view) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vibrator.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE));
            else {
                VibrationEffect effect = VibrationEffect.createOneShot(25, 1);
                vibrator.vibrate(effect);
            }

            state.onLongPress((MyImageButton) view);

            return true;
        }
    }
}
