package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.R;

public class DeleteButtonFactory extends LayoutButtonFactory {
    private Media media;

    public DeleteButtonFactory(MyActivity activity, LinearLayout layout, int layoutID, Media media) {
        super(activity, layout, layoutID);
        this.media = media;
    }

    public ImageButton create() {
        ImageButton button = layout.findViewById(layoutID);
        Drawable icon = ContextCompat.getDrawable(activity, R.drawable.trash);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilesManager manager = FilesManager.getInstance(activity);
                System.out.println("Removing: " + media.getFullPath());
                manager.removeFile(media.getFullPath());
                manager.removeFile(media.getPath() + media.getFilename());
                activity.onBackPressed();
            }
        });
        button.setImageDrawable(resizeDrawable(icon));
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }
}
