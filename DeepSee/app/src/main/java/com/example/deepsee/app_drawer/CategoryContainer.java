package com.example.deepsee.app_drawer;

import static android.view.View.GONE;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;

import java.util.List;

public class CategoryContainer extends RecyclerView.ViewHolder {
    public TextView category_name;
    private RecyclerView apps;
    private PackageManager pm;
    public Context con;
    private boolean open;
    public CategoryContainer(@NonNull View itemView, PackageManager pm) {
        super(itemView);
        this.pm = pm;

        //Find reference to RecyclerView and TextView within category_card layout
        this.apps = itemView.findViewById(R.id.apps_recycler);
        this.category_name = itemView.findViewById(R.id.category_grid_name);
        this.open = false;
        itemView.findViewById(R.id.category_grid_name).setOnClickListener(view -> toggleCategoryView());
    }

    public void initRecyclerView(List<ApplicationInfo> apps){
        this.apps.setAdapter(new AppsAdapter(con, apps, pm));
        this.apps.setLayoutManager(new GridLayoutManager(con, Math.min(Math.max(3, apps.size()/3), 6)));
    }

    public void toggleCategoryView(){
        open = !open;
        apps.setVisibility(open ? View.VISIBLE : GONE);
    }
}
