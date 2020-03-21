package com.example.cripto_photoaffix.Factories.ButtonFactories.ViewerButtons;

import android.view.View;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.Commands.ShareCommand;
import com.example.cripto_photoaffix.Factories.ButtonFactories.LayoutButtonFactory;

public class ShareButtonFactory extends LayoutButtonFactory {
    public ShareButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    @Override
    public View.OnClickListener listener() {
        return new ButtonListener(new ShareCommand());
    }
}
