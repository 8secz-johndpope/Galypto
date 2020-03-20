package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.cripto_photoaffix.Commands.StoreCommand;

public class StoreButtonFactory extends LayoutButtonFactory {
    public StoreButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    public ImageButton create() {

        ImageButton button = layout.findViewById(layoutID);

        button.setOnClickListener(new ButtonListener(new StoreCommand()));
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }
}
