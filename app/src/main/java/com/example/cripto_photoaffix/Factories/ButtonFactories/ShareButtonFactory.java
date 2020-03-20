package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.cripto_photoaffix.Commands.ShareCommand;

public class ShareButtonFactory extends LayoutButtonFactory {
    public ShareButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    public ImageButton create() {
        ImageButton button = layout.findViewById(layoutID);

        button.setOnClickListener(new ButtonListener(new ShareCommand()));

        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }
}
