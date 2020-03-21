package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.view.View;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.Commands.ShareCommand;

public class ShareButtonFactory extends LayoutButtonFactory {
    public ShareButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    @Override
    public View.OnClickListener listener() {
        return new ButtonListener(new ShareCommand());
    }
}
