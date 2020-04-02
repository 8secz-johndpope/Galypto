package com.example.cripto_photoaffix.Activities.GalleryActivities.RecyclerViewComponents;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.R;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Media> media;
    private LayoutInflater inflater;
    private View.OnLongClickListener longClickListener;
    private View.OnClickListener clickListener;

    public RecyclerViewAdapter(Context context, List<Media> media) {
        inflater = LayoutInflater.from(context);
        this.media = media;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyImageButton button = (MyImageButton) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewitem, parent, false);

        button.setMinimumHeight(getScreenHeigth()/7);
        button.setMinimumWidth((int)(getScreenWidth()/3.5));
        button.setMaxWidth((int)(getScreenWidth()/3.5));
        button.setMaxHeight(getScreenHeigth()/7);

        return new ViewHolder(button);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setMedia(media.get(position));
        holder.image.setOnClickListener(clickListener);
        holder.image.setOnLongClickListener(longClickListener);
    }

    @Override
    public int getItemCount() {
        return media.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    /**
     * Retorna el ancho de la pantalla.
     * @return Retorna el ancho de la pantalla.
     */
    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ActivityTransferer.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    /**
     * Retorna el largo de la pantalla.
     * @return Retorna el largo de la pantalla.
     */
    private int getScreenHeigth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ActivityTransferer.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }
}
