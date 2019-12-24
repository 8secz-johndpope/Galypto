package com.example.cripto_photoaffix.Activities;

import android.graphics.Color;
import android.os.Bundle;
import com.example.cripto_photoaffix.Visitors.Visitor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.cripto_photoaffix.R;

public class GalleryActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initialize();
    }

    private void initialize() {
        GridLayout gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(3);
        gridLayout.setRowCount(40);

        for (int i = 0; i < 100; i++) {
            ImageButton button = new ImageButton(this);
            button.setImageResource(R.drawable.pic);
            button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            button.setBackgroundColor(Color.WHITE);
            gridLayout.addView(button, 500, 500);
        }
    }

    public void accept(Visitor visitor) {}
}
