package com.example.deepsee.app_drawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;

public class AppContainer extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView name;
    public Context con;

    public AppContainer(@NonNull View itemView) {
        super(itemView);
        this.con = itemView.getContext();
        this.icon = itemView.findViewById(R.id.app_icon);
        this.name = itemView.findViewById(R.id.app_name);
    }
}
