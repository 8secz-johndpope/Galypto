package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.DeleteCommand;
import com.example.cripto_photoaffix.Commands.RemoveDecryptedMediaCommand;
import com.example.cripto_photoaffix.Commands.ShareMultipleCommand;
import com.example.cripto_photoaffix.Commands.StoreCommand;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.LoginIntentFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Gallery;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.Visitors.AuthenticationVisitors.ActivityVisitor;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaOpenerVisitor;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
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
    private boolean openedImage;
    private GridLayout gridLayout;
    private Map<Media, MyImageButton> buttons;
    private List<FloatingActionButton> actionButtons;

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

    public void accept(ActivityVisitor activityVisitor) {}

    private void initialize() {
        DataTransferer transferer = DataTransferer.getInstance();
        gallery = (Gallery)transferer.getData();

        gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(3);

        gridLayout.setRowCount(gallery.getMedia().size()/3 + 1);

        updateButtons();
    }

    private void updateButtons() {
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

                    button.setOnClickListener(new ButtonOpenerListener(button));
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

    private void hideActionButtons() {
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

    private void showActionButtons() {
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

        if (!openedImage) {
            Command removeDecryptedVideos = new RemoveDecryptedMediaCommand();
            removeDecryptedVideos.execute();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        ActivityTransferer activityTransferer = ActivityTransferer.getInstance();
        activityTransferer.setActivity(this);

        if (!openedImage) {
            IntentFactory factory = new LoginIntentFactory();
            startActivity(factory.create());
            finish();
        }
        else {
            updateButtons();
            openedImage = false;
        }
    }

    private class ButtonOpenerListener implements View.OnClickListener {
        private MyImageButton button;

        public ButtonOpenerListener(MyImageButton button) {
            this.button = button;
        }

        @Override
        public void onClick(View v) {
            MediaVisitor visitor = new MediaOpenerVisitor();
            Media buttonMedia = button.getMedia();
            buttonMedia.accept(visitor);
            openedImage = true;
        }
    }

    private class ButtonSelectorListener implements View.OnClickListener {
        private MyImageButton button;

        public ButtonSelectorListener(MyImageButton button) {
            this.button = button;
        }

        @Override
        public void onClick(View v) {
            if (button.isSelected()) {
                button.setSelected(false);
                button.setAlpha(1f);
            }
            else {
                button.setSelected(true);
                button.setAlpha(0.5f);
            }
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
                List<Media> galleryMedia = gallery.getMedia();
                MyImageButton butt;
                int size = galleryMedia.size();
                Media media;

                for (int i = 0; i < size; i++) {
                    media = galleryMedia.get(i);

                    butt = buttons.get(media);

                    if (butt != null)
                        butt.setOnClickListener(new ButtonSelectorListener(butt));
                }

                showActionButtons();
            }
            else {
                List<Media> galleryMedia = gallery.getMedia();
                MyImageButton butt;
                int size = galleryMedia.size();
                Media media;

                for (int i = 0; i < size; i++) {
                    media = galleryMedia.get(i);

                    butt = buttons.get(media);

                    if (butt != null) {
                        butt.setSelected(false);
                        butt.setAlpha(1f);
                        butt.setOnClickListener(new ButtonOpenerListener(butt));
                    }
                }

                hideActionButtons();
            }
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

            for (int i = 0; i < size; i++) {
                media = galleryMedia.get(i);

                if (buttons.get(media).isSelected())
                    task.addMedia(media);
            }

            task.execute();

            updateButtons();

            hideActionButtons();
        }
    }
}
