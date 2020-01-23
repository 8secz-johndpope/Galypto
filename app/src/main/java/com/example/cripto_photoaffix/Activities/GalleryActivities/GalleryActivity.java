package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.OpenerState;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.SelectorState;
import com.example.cripto_photoaffix.Activities.GalleryActivities.GalleryActivityStates.State;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.DeleteCommand;
import com.example.cripto_photoaffix.Commands.ShareMultipleCommand;
import com.example.cripto_photoaffix.Commands.StoreCommand;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Gallery;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.Toolbar;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import com.example.cripto_photoaffix.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class GalleryActivity extends MyActivity {

    private Gallery gallery;
    private GridLayout gridLayout;
    private Map<Media, MyImageButton> buttons;
    private List<FloatingActionButton> actionButtons;
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttons = new HashMap<Media, MyImageButton>();

        initialize();

        initializeFloatingButtons();
    }

    public void changeState(State state) {
        this.state = state;
    }

    public void unselectAllButtons() {
        for (MyImageButton button: buttons.values()) {
            button.setSelected(false);
            button.setAlpha(1f);
        }
    }

    @Override
    public void refresh() {
        List<Media> galleryMedia = gallery.getMedia();
        MyImageButton button;
        FilesManager manager = FilesManager.getInstance();
        Queue<Media> toRemove = new LinkedTransferQueue<Media>();
        View.OnLongClickListener longClickListener = new LongClickListener();

        Media media;
        int size = galleryMedia.size();

        for (int i = 0; i < size; i++) {
            media = galleryMedia.get(i);

            if (manager.exists(media.getFullPath())) {

                if (buttons.get(media) == null) {
                    button = new MyImageButton(media);

                    button.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    gridLayout.addView(button, getScreenWidth() / 3, getScreenHeigth() / 6);

                    button.setOnClickListener(new ButtonListener(button));
                    button.setOnLongClickListener(longClickListener);

                    buttons.put(media, button);

                    button.setBackgroundColor(Color.BLACK);
                }

            }
            else {
                toRemove.add(media);

                gridLayout.removeView(buttons.get(media));

                buttons.remove(media);
            }
        }

        while (!toRemove.isEmpty())
            gallery.remove(toRemove.poll());
    }

    public void hideActionButtons() {
        CoordinatorLayout.LayoutParams params;
        int size = actionButtons.size();
        FloatingActionButton b;


        for (int i = 0; i < size; i++) {
            b = actionButtons.get(i);

            params = (CoordinatorLayout.LayoutParams) b.getLayoutParams();
            params.setAnchorId(View.NO_ID);
            b.setLayoutParams(params);
            b.hide();
        }
    }

    public void showActionButtons() {
        CoordinatorLayout.LayoutParams params;
        int size = actionButtons.size();
        FloatingActionButton b;

        for (int i = 0; i < size; i++) {
            b = actionButtons.get(i);

            params = (CoordinatorLayout.LayoutParams) b.getLayoutParams();
            params.setBehavior(new FloatingActionButton.Behavior());
            params.setAnchorId(R.id.app_bar);
            b.setLayoutParams(params);
            b.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        state.onPause();
    }

    @Override
    public void onRestart() {
        super.onRestart();

        ActivityTransferer activityTransferer = ActivityTransferer.getInstance();
        activityTransferer.setActivity(this);

        state.onRestart();
    }

    public void accept(ActivityVisitor activityVisitor) {
        activityVisitor.visit(this);
    }

    private void initialize() {
        DataTransferer transferer = DataTransferer.getInstance();
        gallery = (Gallery)transferer.getData();

        gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(3);

        gridLayout.setRowCount(gallery.getMedia().size()/3 + 1);

        refresh();

        state = new OpenerState();
    }

    private void initializeFloatingButtons() {
        actionButtons = new ArrayList<FloatingActionButton>();

        FloatingActionButton button = findViewById(R.id.remove);
        button.setOnClickListener(new FloatingButtonListener(new DeleteCommand()));
        button.hide();
        actionButtons.add(button);

        button = findViewById(R.id.store);
        button.setOnClickListener(new FloatingButtonListener(new StoreCommand()));
        button.hide();
        actionButtons.add(button);

        button = findViewById(R.id.share);
        button.setOnClickListener(new FloatingButtonListener(new ShareMultipleCommand()));
        button.hide();
        actionButtons.add(button);
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    private int getScreenHeigth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }

    private class ButtonListener implements View.OnClickListener {
        private MyImageButton button;

        public ButtonListener(MyImageButton button) {
            this.button = button;
        }

        @Override
        public void onClick(View v) {
            state.touch(button);
        }
    }

    private class LongClickListener implements View.OnLongClickListener {
        public boolean onLongClick(View view) {
            view.setSelected(true);
            view.setAlpha(0.5f);

            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vibrator.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE));
            else {
                VibrationEffect effect = VibrationEffect.createOneShot(25, 1);
                vibrator.vibrate(effect);
            }

            if (!actionButtons.get(0).isShown()) {
                state = new SelectorState(state);
                showActionButtons();
            }
            else
                state.back();

            return true;
        }
    }

    private class FloatingButtonListener implements View.OnClickListener {
        private Command task;

        public FloatingButtonListener(Command task) {
            this.task = task;
        }

        @Override
        public void onClick(View v) {
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

            hideActionButtons();

            state = new OpenerState();
        }
    }
}
