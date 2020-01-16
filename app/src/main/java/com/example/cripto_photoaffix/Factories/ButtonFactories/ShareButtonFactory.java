package com.example.cripto_photoaffix.Factories.ButtonFactories;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.ShareVisitor;

public class ShareButtonFactory extends LayoutButtonFactory {
    public ShareButtonFactory(LinearLayout layout, int layoutID) {
        super(layout, layoutID);
    }

    public ImageButton create() {
        MyActivity activity = ActivityTransferer.getInstance().getActivity();

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
            MyActivity activity = ActivityTransferer.getInstance().getActivity();
            Media media = (Media) DataTransferer.getInstance().getData();

            MediaVisitor visitor = new ShareVisitor();

            media.accept(visitor);
        }
    }
}
