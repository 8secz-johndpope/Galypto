package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Commands.MediaCommands.MediaCommand;
import com.example.cripto_photoaffix.Commands.MediaCommands.DeleteMediaCommand;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Gallery.Media;

public class DeleteButtonFactory extends LayoutButtonFactory {

    public DeleteButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    public ImageButton create() {
        final MyActivity activity = ActivityTransferer.getInstance().getActivity();
        final Media media = (Media)DataTransferer.getInstance().getData();

        ImageButton button = layout.findViewById(layoutID);
        Drawable icon = ContextCompat.getDrawable(activity, android.R.drawable.ic_menu_delete);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaCommand delete = new DeleteMediaCommand();
                delete.execute(media);
                activity.onBackPressed();
            }
        });
        button.setImageDrawable(icon);
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }
}
