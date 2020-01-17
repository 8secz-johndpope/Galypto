package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.widget.LinearLayout;

public abstract class LayoutButtonFactory extends ButtonFactory {
    protected int layoutID;
    protected LinearLayout layout;

    protected LayoutButtonFactory(LinearLayout layout, int layoutID) {
        this.layoutID = layoutID;
        this.layout = layout;
    }
}
