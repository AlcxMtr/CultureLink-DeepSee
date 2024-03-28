package com.example.deepsee.app_drawer;

import static android.view.View.GONE;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class CategoryContainer extends RecyclerView.ViewHolder {
    public TextView category_name;
    public RecyclerView apps;
    private PackageManager pm;

    private AppsAdapter adapter;
    public Context con;
    private boolean open;
    private ImageView arrow;

    private int pos = -1;
    public CategoryContainer(@NonNull View itemView, PackageManager pm) {
        super(itemView);
        this.pm = pm;

        //Find reference to RecyclerView and TextView within category_card layout
        this.apps = itemView.findViewById(R.id.apps_recycler);
        this.category_name = itemView.findViewById(R.id.category_grid_name);
        this.open = true;
        this.arrow = itemView.findViewById(R.id.category_arrow);
        itemView.findViewById(R.id.category_grid_name).setOnClickListener(view -> toggleCategoryView());
    }


    public AppsAdapter getAdapter() {
        return adapter;
    }

    public void initRecyclerView(List<ApplicationInfo> apps, AppsAdapter adapter){
        this.adapter = adapter;
        this.apps.setAdapter(adapter);
        this.apps.setLayoutManager(new GridLayoutManager(con, Math.min(Math.max(3, apps.size()/3), 6)));
    }



    public void setPos(int pos) {
        if (this.pos == -1)
            this.pos = pos;
    }
    public void toggleCategoryView(){
        open = !open;
        apps.setVisibility(open ? View.VISIBLE : GONE);
        arrow.setRotation(open ? 90 : 180);
    }
}
