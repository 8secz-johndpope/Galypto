package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.Opener;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.State;
import com.example.cripto_photoaffix.Activities.GalleryActivities.RecyclerViewComponents.RecyclerViewAdapter;
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
import android.util.DisplayMetrics;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import com.example.cripto_photoaffix.R;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GalleryActivity extends MyActivity {

    private Gallery gallery;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Media> mediaList;
    private List<ImageButton> actionButtons;
    private View actionButtonsView;
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        List<Media> media = gallery.getMedia();

        for (Media m: media)
            m.deselect();

        recyclerViewAdapter.update();
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
        List<Media> toRemove = new ArrayList<Media>(mediaList);

        Media media;
        int size = galleryMedia.size();

        for (int i = 0; i < size; i++) {
            media = galleryMedia.get(i);

            if (mediaList.contains(media))           //Si hay un boton en la pantalla, lo saco de
                toRemove.remove(media);              //la lista a eliminar botones.
        }

        size = toRemove.size();

        for (int j = 0; j < size; j++) {              //Elimino todos los botones que hay que eliminar.
            media = toRemove.get(j);
            mediaList.remove(media);
            recyclerViewAdapter.remove(media);
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

        for (int i = 0; i < size; i++) {
            media = galleryMedia.get(i);

            if (media.isSelected()) {
                task.addMedia(media);

                media.deselect();
            }
        }

        recyclerViewAdapter.update();

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

        RecyclerView recyclerView = findViewById(R.id.grid_layout);
        recyclerView.setHorizontalScrollBarEnabled(false);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        System.out.println(recyclerView);
        recyclerView.setLayoutManager(manager);

        recyclerViewAdapter = new RecyclerViewAdapter(this, gallery.getMedia());
        recyclerViewAdapter.setOnClickListener(new ButtonListener());
        recyclerViewAdapter.setOnLongClickListener(new LongClickListener());
        recyclerView.setAdapter(recyclerViewAdapter);

        mediaList = new ArrayList<Media>(gallery.getMedia());

        refresh();

        state = new Opener();
    }

    /**
     * Inicializa los botones flotantes (compartir, eliminar y borrar).
     */
    private void initializeFloatingButtons() {
        actionButtons = new ArrayList<ImageButton>();

        View layout = findViewById(R.id.constraintLay);

        ButtonFactory factory = new GalleryDeleteButtonFactory(layout, R.id.delete);
        ImageButton button = factory.create();
        actionButtons.add(button);

        factory = new GalleryStoreButtonFactory(layout, R.id.store);
        button = factory.create();
        actionButtons.add(button);

        factory = new GalleryShareButtonFactory(layout, R.id.share);
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
            recyclerViewAdapter.update();
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
            recyclerViewAdapter.update();

            return true;
        }
    }
}
