package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons.VideoButtons;

import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.Activities.GalleryActivities.ViewerStates.State;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.PauseVisitor;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.PlayVisitor;

public class PlayPauseButtonFactory extends LayoutButtonFactory {
    private State state;

    public PlayPauseButtonFactory(LinearLayout layout, int layoutID, State state) {
        super(layout, layoutID);
        this.state = state;
    }

    @Override
    public ImageButton create() {
        ImageButton created = super.create();
        created.setBackgroundResource(R.drawable.play);
        return created;
    }

    @Override
    protected View.OnClickListener listener() {
        return new Listener();
    }

    private class Listener implements View.OnClickListener {
        private boolean playing;

        public Listener() {
            playing = false;
        }

        @Override
        public void onClick(View v) {
            if (playing) {
                ActivityTransferer.getInstance().getActivity().accept(new PauseVisitor());
                v.setBackgroundResource(R.drawable.play);
                playing = false;
            }
            else {
                ActivityTransferer.getInstance().getActivity().accept(new PlayVisitor());
                v.setBackgroundResource(R.drawable.pause);
                playing = true;
            }
        }
    }
}
