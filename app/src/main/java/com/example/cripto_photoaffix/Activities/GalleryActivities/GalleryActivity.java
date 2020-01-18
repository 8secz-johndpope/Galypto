package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.graphics.Color;
import android.os.Bundle;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.RemoveDecryptedMediaCommand;
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
import android.util.DisplayMetrics;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.gridlayout.widget.GridLayout;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import com.example.cripto_photoaffix.R;
import java.util.HashMap;
import java.util.LinkedList;
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

        Switch selector = findViewById(R.id.selector);
        selector.setOnCheckedChangeListener(new CheckListener());
    }

    private void updateButtons() {
        List<Media> galleryMedia = gallery.getMedia();
        MyImageButton button;
        FilesManager manager = FilesManager.getInstance();
        Queue<Media> toRemove = new LinkedTransferQueue<Media>();

        for (Media media: galleryMedia) {
            if (manager.exists(media.getFullPath())) {
                if (buttons.get(media) == null) {
                    System.out.println(media.getFullPath());
                    button = new MyImageButton(media);

                    button.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    gridLayout.addView(button, getScreenWidth() / 3, getScreenHeigth() / 6);

                    button.setOnClickListener(new ButtonOpenerListener(button));

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
        actionButtons = new LinkedList<FloatingActionButton>();

        FloatingActionButton button = findViewById(R.id.remove);
        button.setOnClickListener(new FloatingButtonListener());
        button.hide();
        actionButtons.add(button);

        button = findViewById(R.id.store);
        button.setOnClickListener(new FloatingButtonListener());
        button.hide();
        actionButtons.add(button);

        button = findViewById(R.id.share);
        button.setOnClickListener(new FloatingButtonListener());
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
            if (button.isSelected())
                button.setSelected(false);
            else
                button.setSelected(true);
        }
    }

    private class CheckListener implements Switch.OnCheckedChangeListener {

        public void onCheckedChanged(CompoundButton button, boolean checked) {
            CoordinatorLayout.LayoutParams params;
            if (checked) {
                List<Media> galleryMedia = gallery.getMedia();
                MyImageButton butt;

                for (Media media: galleryMedia) {
                    butt = buttons.get(media);
                    if (butt != null)
                        butt.setOnClickListener(new ButtonSelectorListener(butt));
                }

                for (FloatingActionButton b: actionButtons) {
                    params = (CoordinatorLayout.LayoutParams) b.getLayoutParams();
                    params.setBehavior(new FloatingActionButton.Behavior());
                    params.setAnchorId(R.id.app_bar);
                    b.setLayoutParams(params);
                    b.show();
                }
            }
            else {
                List<Media> galleryMedia = gallery.getMedia();
                MyImageButton butt;

                for (Media media: galleryMedia) {
                    butt = buttons.get(media);
                    if (butt != null) {
                        butt.setSelected(false);
                        butt.setOnClickListener(new ButtonOpenerListener(butt));
                    }
                }

                for (FloatingActionButton b: actionButtons) {
                    params = (CoordinatorLayout.LayoutParams) b.getLayoutParams();
                    params.setAnchorId(View.NO_ID);
                    b.setLayoutParams(params);
                    b.hide();
                }
            }

        }
    }

    private class FloatingButtonListener implements View.OnClickListener {
        private Command task;

        @Override
        public void onClick(View v) {
            List<Media> galleryMedia = gallery.getMedia();

            for (Media media: galleryMedia) {
                if (buttons.get(media).isSelected())
                    task.execute();
            }

            updateButtons();
        }
    }
}
