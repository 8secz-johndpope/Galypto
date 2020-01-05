package com.example.cripto_photoaffix.Factories.ButtonFactories;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.ShareIntentFactory;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.R;
import java.io.File;

public class ShareButtonFactory extends LayoutButtonFactory {
    public ShareButtonFactory(MyActivity activity, LinearLayout layout, int layoutID) {
        super(activity, layout, layoutID);
    }

    public ImageButton create() {
        ImageButton button = layout.findViewById(layoutID);
        Drawable icon = ContextCompat.getDrawable(activity, R.drawable.share);

        button.setOnClickListener(new Listener());

        button.setImageDrawable(resizeDrawable(icon));
        button.setBackgroundColor(Color.TRANSPARENT);

        return button;
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //Preguntar si esto esta bien. No me gusta para nada castear. Podria crear
            //para cada tipo de datos un transferer.
            Media media = (Media) DataTransferer.getInstance().getData();
            File file = media.share(activity.getCacheDir().getPath() + "/share/");

            IntentFactory factory = new ShareIntentFactory();
            Intent intent = factory.create();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpg");

            Uri path = FileProvider.getUriForFile(activity,
                    "com.example.cripto_photoaffix.fileprovider", file);

            intent.putExtra(Intent.EXTRA_STREAM, path);

            activity.startActivity(Intent.createChooser(intent, "Share via:"));
        }
    }
}
