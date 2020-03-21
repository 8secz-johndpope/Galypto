package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public abstract class LayoutButtonFactory extends ButtonFactory {
    private int layoutID;
    private LinearLayout layout;

    protected LayoutButtonFactory(LinearLayout layout, int layoutID) {
        this.layoutID = layoutID;
        this.layout = layout;
    }

    public ImageButton create() {
        ImageButton button = layout.findViewById(layoutID);

        button.setBackgroundColor(Color.TRANSPARENT);

        button.setOnClickListener(listener());

        return button;
    }

    protected abstract View.OnClickListener listener();
}
