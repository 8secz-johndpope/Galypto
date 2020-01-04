package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.R;

public class ShareButtonFactory extends LayoutButtonFactory {
    public ShareButtonFactory(MyActivity activity, LinearLayout layout, int layoutID) {
        super(activity, layout, layoutID);
    }

    public ImageButton create() {
        ImageButton button = layout.findViewById(layoutID);
        Drawable icon = ContextCompat.getDrawable(activity, R.drawable.share);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        button.setImageDrawable(resizeDrawable(icon));
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }
}
