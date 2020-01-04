package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.R;

public class StoreButtonFactory extends LayoutButtonFactory {
    public StoreButtonFactory(MyActivity activity, LinearLayout layout, int layoutID) {
        super(activity, layout, layoutID);
    }

    public ImageButton create() {
        ImageButton button = layout.findViewById(layoutID);
        Drawable icon = ContextCompat.getDrawable(activity, R.drawable.restore);

        button.setOnClickListener(new Listener(button));

        button.setImageDrawable(resizeDrawable(icon));
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }

    private class Listener implements View.OnClickListener {
        private ImageButton button;

        public Listener(ImageButton button) {
            this.button = button;
        }

        @Override
        public void onClick(View v) {
            button.animate();
        }
    }

    private class MyAnimation extends Animation {

    }
}
