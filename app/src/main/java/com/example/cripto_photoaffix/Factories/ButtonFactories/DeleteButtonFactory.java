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
import com.example.cripto_photoaffix.Commands.DeleteCommand;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MediaTransferer;

public class DeleteButtonFactory extends LayoutButtonFactory {

    public DeleteButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    public ImageButton create() {
        final MyActivity activity = ActivityTransferer.getInstance().getActivity();

        ImageButton button = layout.findViewById(layoutID);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media media = MediaTransferer.getInstance().getMedia();

                Command delete = new DeleteCommand();
                delete.addMedia(media);
                delete.execute();
                activity.onBackPressed();
            }
        });
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }
}
