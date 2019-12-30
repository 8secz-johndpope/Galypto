package com.example.cripto_photoaffix.Activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.ImageViewerIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Gallery;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.Visitors.Visitor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cripto_photoaffix.R;
import java.util.List;

public class GalleryActivity extends MyActivity {

    private Gallery gallery;

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

        initialize();
    }

    public void accept(Visitor visitor) {}

    private void initialize() {
        DataTransferer transferer = DataTransferer.getInstance();
        gallery = (Gallery)transferer.getData();

        GridLayout gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(3);
        gridLayout.setRowCount(gallery.getPictures().size()/3 + 1);

        List<Bitmap> bitmaps = gallery.getPictures();

        for (Bitmap picture : bitmaps) {
            MyImageButton button = new MyImageButton(picture, this);
            button.setScaleType(ImageView.ScaleType.CENTER_CROP);
            button.setBackgroundColor(Color.WHITE);
            gridLayout.addView(button, getScreenWidth()/3, getScreenHeigth()/6);
            button.setOnClickListener(new ButtonListener(button));
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
            Toast.makeText(GalleryActivity.this, "Opening Image!!!!", Toast.LENGTH_SHORT).show();
            IntentFactory factory = new ImageViewerIntentFactory(GalleryActivity.this);

            DataTransferer transferer = DataTransferer.getInstance();
            transferer.setData(button.getBitmap());

            startActivity(factory.create());
        }
    }
}
