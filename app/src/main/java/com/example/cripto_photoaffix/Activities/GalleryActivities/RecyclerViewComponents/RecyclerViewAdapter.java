package com.example.cripto_photoaffix.Activities.GalleryActivities.RecyclerViewComponents;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Media media;
    private LayoutInflater inflater;
    private AdapterView.OnItemClickListener listener;

    public RecyclerViewAdapter(Context context, Media media) {
        inflater = LayoutInflater.from(context);
        this.media = media;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerviewitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setBackground(new BitmapDrawable(ActivityTransferer.getInstance().getActivity().getResources(), media.getPreview()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
