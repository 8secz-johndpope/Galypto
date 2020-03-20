package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.DeleteCommand;

public class DeleteButtonFactory extends LayoutButtonFactory {

    public DeleteButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    public ImageButton create() {
        ImageButton button = layout.findViewById(layoutID);

        button.setOnClickListener(new DeleteListener());
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }

    private class DeleteListener extends ButtonListener {
        private DeleteListener() {
            super(new DeleteCommand());
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);

            MyActivity activity = ActivityTransferer.getInstance().getActivity();
            activity.onBackPressed();
        }
    }
}
