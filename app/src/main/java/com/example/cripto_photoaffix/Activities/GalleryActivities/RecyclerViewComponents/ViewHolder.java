package com.example.cripto_photoaffix.Activities.GalleryActivities.RecyclerViewComponents;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cripto_photoaffix.MyImageButton;

public class ViewHolder extends RecyclerView.ViewHolder {
    public MyImageButton image;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.image = (MyImageButton) itemView;
    }
}
