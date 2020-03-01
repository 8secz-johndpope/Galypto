package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.ShareCommand;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MediaTransferer;

public class ShareButtonFactory extends LayoutButtonFactory {
    public ShareButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    public ImageButton create() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

        ImageButton button = layout.findViewById(layoutID);
        Drawable icon = ContextCompat.getDrawable(activity, android.R.drawable.ic_menu_share);

        button.setOnClickListener(new Listener());

        button.setImageDrawable(icon);
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Media media = MediaTransferer.getInstance().getMedia();

            Command share = new ShareCommand();
            share.addMedia(media);
            share.execute();
        }
    }
}
