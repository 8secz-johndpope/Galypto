package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons.VideoButtons;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ViewerStates.State;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;
import com.example.cripto_photoaffix.R;

public class PlayPauseButton extends LayoutButtonFactory {
    private State state;

    public PlayPauseButton(LinearLayout layout, int layoutID, State state) {
        super(layout, layoutID);
    }

    @Override
    protected View.OnClickListener listener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.actOnVideo((ImageButton) v);
                v.setBackgroundResource(R.drawable.pause);
            }
        };
    }
}
