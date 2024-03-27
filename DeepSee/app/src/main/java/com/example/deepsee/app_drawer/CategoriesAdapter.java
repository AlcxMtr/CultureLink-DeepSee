package com.example.deepsee.app_drawer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryContainer>{
    private HashMap<Integer, List<ApplicationInfo>> categories;
    private Integer[] keys;
    private PackageManager pm;


    public CategoriesAdapter(HashMap<Integer, List<ApplicationInfo>> categories, PackageManager pm) {
        this.categories = categories;
        this.keys = categories.keySet().toArray(new Integer[]{});
        this.pm = pm;
    }

    /*
    * Creates CategoryContainer to hold one category card*/
    @NonNull
    @Override
    public CategoryContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View category_card = inflater.inflate(R.layout.category_card_open, parent, false);

        CategoryContainer v = new CategoryContainer(category_card, pm);
        v.con = context;
        return v;
    }



    /*
    * Bind Category name and app list to category card*/
    @Override
    public void onBindViewHolder(@NonNull CategoryContainer holder, int position) {
        //Initialize recycler view for holding apps
        holder.initRecyclerView(categories.get(keys[position]));
        String cat = (String)ApplicationInfo.getCategoryTitle(holder.con, keys[position]);

        if (Objects.equals(cat, null))
            cat = "Utility";
        holder.category_name.setText(cat);

        //Open first category
        if (position == 0){
            holder.toggleCategoryView();
        }
    }

    @Override
    public int getItemCount() {
        return keys.length;
    }
}
