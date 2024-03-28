package com.example.deepsee.app_drawer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.AppDrawerFragment;
import com.example.deepsee.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryContainer>{
    private HashMap<Integer, List<ApplicationInfo>> categories;
    private Integer[] keys;
    private PackageManager pm;
    private RecyclerView rv;
    private List<List<Drawable>> icons;
    private List<List<CharSequence>> labels;
    private List<List<Intent>> launchers;

    private List<CharSequence> categoryLabels;
    private final List<Consumer<Boolean>> toggleCategoriesDeleteMode;

    public CategoriesAdapter(HashMap<Integer, List<ApplicationInfo>> categories, PackageManager pm, RecyclerView rv) {
        this.categories = categories;
        this.keys = categories.keySet().toArray(new Integer[]{});
        this.pm = pm;
        this.rv = rv;
        toggleCategoriesDeleteMode = new LinkedList<>();

        initiateData();
    }

    /*
    * Creates CategoryContainer to hold one category card*/
    @NonNull
    @Override
    public CategoryContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View category_card = inflater.inflate(R.layout.category_card, parent, false);

        CategoryContainer v = new CategoryContainer(category_card, pm);
//        toggleCategoriesDeleteMode.add((mode) -> v.getAdapter().setDeleteMode(mode));

        v.con = context;
        return v;
    }

    private void initiateData() {
        icons = new ArrayList<>(categories.keySet().size());
        labels = new ArrayList<>(categories.keySet().size());
        launchers = new ArrayList<>(categories.keySet().size());
        categoryLabels = new ArrayList<>(keys.length);
        int pos = 0;
        for (Integer categoryNum : keys){
            icons.add(new ArrayList<>());
            labels.add(new ArrayList<>());
            launchers.add(new ArrayList<>());
            for (ApplicationInfo info : categories.get(categoryNum)){
                try {
                    icons.get(pos).add(pm.getApplicationIcon(info.packageName));
                } catch (PackageManager.NameNotFoundException e) {
                    System.out.println("Icon not found for "+info.packageName+"...");
                }
                try {
                    labels.get(pos).add(pm.getApplicationLabel(pm.getApplicationInfo(info.packageName,0)));
                } catch (PackageManager.NameNotFoundException e) {
                    System.out.println("Label not found for "+info.packageName+"...");
                }
                launchers.get(pos).add(new Intent(pm.getLaunchIntentForPackage(info.packageName)));
            }
            categoryLabels.add(ApplicationInfo.getCategoryTitle(rv.getContext(), categoryNum));
            if (categoryLabels.get(pos) == null){
                categoryLabels.set(pos, "Utility");
            }
            pos++;
        }
    }

    /*
    * Bind Category name and app list to category card*/
    @Override
    public void onBindViewHolder(@NonNull CategoryContainer holder, int position) {
        toggleCategoriesDeleteMode.add((mode) -> holder.getAdapter().setDeleteMode(mode));
        AppsAdapter adapter = new AppsAdapter(rv.getContext(), categories.get(keys[position]), icons.get(position), labels.get(position), launchers.get(position), this::toggleDeleteMode);
        //Initialize recycler view for holding apps
        holder.initRecyclerView(categories.get(keys[position]), adapter);

        holder.setPos(position);
        holder.category_name.setText(categoryLabels.get(position));
        holder.apps.setRecycledViewPool(rv.getRecycledViewPool());
        holder.apps.setHasFixedSize(true);
        holder.getAdapter().clearToggleList();
    }

    //Iterate through all categories and run their respective toggleDeleteMode functions
    private void toggleDeleteMode(){
        AppDrawerFragment.deleteMode = !AppDrawerFragment.deleteMode;
        for (Consumer<Boolean> f:toggleCategoriesDeleteMode){
            f.accept(AppDrawerFragment.deleteMode);
        }
    }

    @Override
    public int getItemCount() {
        return keys.length;
    }
}
