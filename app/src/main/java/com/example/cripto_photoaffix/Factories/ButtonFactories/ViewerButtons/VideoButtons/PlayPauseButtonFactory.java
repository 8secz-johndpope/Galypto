package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons.VideoButtons;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.VideoButton;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.PauseVisitor;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ViewerVisitors.PlayVisitor;

public class PlayPauseButtonFactory extends LayoutButtonFactory {

    public PlayPauseButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
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
        @Override
        public void onClick(View v) {
            VideoButton button = (VideoButton) v;
            if (button.isPlaying()) {
                ActivityTransferer.getInstance().getActivity().accept(new PauseVisitor());
                button.paused();
            }
            else {
                ActivityTransferer.getInstance().getActivity().accept(new PlayVisitor());
                button.playing();
            }
        }
    }
}
