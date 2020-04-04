package com.example.cripto_photoaffix.Activities.GalleryActivities.RecyclerViewComponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MyImageButton;
import com.example.cripto_photoaffix.R;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Media> media;
    private View.OnLongClickListener longClickListener;
    private View.OnClickListener clickListener;

    public RecyclerViewAdapter(Context context, List<Media> media) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.media = media;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyImageButton button = (MyImageButton) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewitem, parent, false);

        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) button.getLayoutParams();
        params.height = parent.getMeasuredHeight()/6;
        params.width = (int)(parent.getMeasuredWidth()/3.5);
        button.setLayoutParams(params);

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

    public void remove(Media media) {
        int position = this.media.indexOf(media);
        this.media.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.media.size());
    }

    public void update() {
        notifyDataSetChanged();
    }
}
