package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons;

import android.view.View;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.DeleteCommand;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;

public class DeleteButtonFactory extends LayoutButtonFactory {

    public DeleteButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    @Override
    protected View.OnClickListener listener() {
        return new DeleteListener();
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
