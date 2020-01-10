package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.graphics.Color;
import android.os.Bundle;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.RemoveDecrypted;
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
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import com.example.cripto_photoaffix.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class GalleryActivity extends MyActivity {

    private Gallery gallery;
    private boolean openedImage;
    private GridLayout gridLayout;
    private Map<String, MyImageButton> pathButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pathButtons = new HashMap<String, MyImageButton>();
        initialize();
    }

    public void accept(ActivityVisitor activityVisitor) {}

    private void initialize() {
        DataTransferer transferer = DataTransferer.getInstance();
        gallery = (Gallery)transferer.getData();

        gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(3);

        List<Media> galleryMedia = gallery.getMedia();
        gridLayout.setRowCount(galleryMedia.size()/3 + 1);

        updateButtons(galleryMedia);
    }

    private void updateButtons(List<Media> galleryMedia) {
        MyImageButton button;
        FilesManager manager = FilesManager.getInstance(this);
        Queue<Media> toRemove = new LinkedTransferQueue<Media>();

        for (Media media : galleryMedia) {
            if (manager.exists(media.getPath())) {
                if (pathButtons.get(media.getPath()) == null) {
                    button = new MyImageButton(media, this);

                    button.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    button.setBackgroundColor(Color.WHITE);

                    gridLayout.addView(button, getScreenWidth() / 3, getScreenHeigth() / 6);

                    button.setOnClickListener(new ButtonListener(button));

                    pathButtons.put(media.getPath(), button);
                }
            }
            else {
                toRemove.add(media);
                pathButtons.remove(media.getPath());
            }
        }

        Media m;

        while (!toRemove.isEmpty()) {
            m = toRemove.poll();
            gallery.remove(m);
            gridLayout.removeView(pathButtons.get(m.getPath()));
        }
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
            MediaVisitor visitor = new MediaOpenerVisitor(GalleryActivity.this);
            Media buttonMedia = button.getMedia();
            buttonMedia.accept(visitor);
            openedImage = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!openedImage) {
            Command removeDecryptedVideos = new RemoveDecrypted(this);
            removeDecryptedVideos.execute();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        if (!openedImage) {
            IntentFactory factory = new LoginIntentFactory(this);
            startActivity(factory.create());
            finish();
        }
        else {
            updateButtons(gallery.getMedia());
            openedImage = false;
        }
    }
}
