package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons.VideoButtons;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.PauseVisitor;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.PlayVisitor;

public class PlayPauseButtonFactory extends LayoutButtonFactory {

    public PlayPauseButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    @Override
    public ImageButton create() {
        ImageButton created = super.create();

        created.setBackgroundResource(R.drawable.playbutton);
        return created;
    }

    @Override
    protected View.OnClickListener listener() {
        return new Listener();
    }

    private class Listener implements View.OnClickListener {
        private boolean playing;

        private Listener() {
            playing = false;
        }

        @Override
        public void onClick(View v) {
            if (playing) {
                ActivityTransferer.getInstance().getActivity().accept(new PauseVisitor());
                playing = false;
                v.setBackgroundResource(R.drawable.playbutton);
            }
            else {
                ActivityTransferer.getInstance().getActivity().accept(new PlayVisitor());
                playing = true;
                v.setBackgroundResource(R.drawable.pausebutton);
            }
        }
    }
}
