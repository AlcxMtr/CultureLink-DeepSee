package com.example.deepsee.app_drawer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.AppDrawerFragment;
import com.example.deepsee.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryContainer>{
    private HashMap<Integer, List<ApplicationInfo>> categories;
    private Integer[] keys;
    private final PackageManager pm;
    private RecyclerView rv;
    /*
    * Prefetched data for recycler view*/
    private List<List<Drawable>> icons;
    private List<List<CharSequence>> labels;
    private List<List<Intent>> launchers;
    private List<CharSequence> categoryLabels;

    //Stack containing apps currently selected to uninstall
    public static Stack<Pair<ApplicationInfo, Integer>> uninstallStack;
    private final List<Consumer<Boolean>> toggleCategoriesDeleteMode;
    private final AppDrawerFragment parent;

    public CategoriesAdapter(HashMap<Integer, List<ApplicationInfo>> categories, PackageManager pm, RecyclerView rv, AppDrawerFragment f) {
        this.categories = categories;
        this.keys = categories.keySet().toArray(new Integer[]{});
        this.pm = pm;
        this.rv = rv;
        toggleCategoriesDeleteMode = new LinkedList<>();
        uninstallStack = new Stack<>();
        this.parent = f;

        f.getView().findViewById(R.id.undo_uninstalling_button).setOnClickListener(this::undoUninstall);
        f.getView().findViewById(R.id.cancel_uninstalling_button).setOnClickListener(this::finishUninstalling);
        f.getView().findViewById(R.id.done_uninstalling_button).setOnClickListener(this::applyUninstall);

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

        CategoryContainer v = new CategoryContainer(category_card);
//        toggleCategoriesDeleteMode.add((mode) -> v.getAdapter().setDeleteMode(mode));

        v.con = context;
        return v;
    }

    //Fetch Application Info data from package manager
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
                categoryLabels.set(pos, "Other");
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
        holder.category_name.setText(categoryLabels.get(position));
//        holder.apps.setRecycledViewPool(rv.getRecycledViewPool());
        holder.apps.setHasFixedSize(true);
        holder.getAdapter().clearToggleList();
    }

    //Toggle the delete mode of the drawer
    private void toggleDeleteMode(){
        applyDeleteMode(!AppDrawerFragment.deleteMode);
    }


    //Set the delete mode for the drawer and iterate through every category container to update them
    private void applyDeleteMode(boolean mode){
        AppDrawerFragment.deleteMode = mode;
        AppDrawerFragment.setDrawerMode(mode, parent);
        for (Consumer<Boolean> f:toggleCategoriesDeleteMode){
            f.accept(mode);
        }
    }

    //Remove an app from uninstall stack and reset it's onClick, etc
    public void undoUninstall(View v){
        if (!uninstallStack.empty()) {
            Pair<ApplicationInfo, Integer> p = uninstallStack.pop();
            int pos = Arrays.binarySearch(keys, p.first.category);
            CategoryContainer container = (CategoryContainer) rv.findViewHolderForAdapterPosition(pos);
            AppsAdapter adapter = container.getAdapter();
            adapter.undoDelete(Objects.requireNonNull(container.apps.findViewHolderForAdapterPosition(p.second)).itemView);
        }
    }

    //Uninstall all apps in uninstall stack
    public void applyUninstall(View v){
        while (!uninstallStack.empty()){
            Pair<ApplicationInfo, Integer> p = uninstallStack.pop();

            int pos = Arrays.binarySearch(keys, p.first.category);
            CategoryContainer container = (CategoryContainer) rv.findViewHolderForAdapterPosition(pos);
            AppsAdapter adapter = container.getAdapter();
            adapter.apps.remove((int)p.second);
            icons.get(pos).remove((int)p.second);
            launchers.get(pos).remove((int)p.second);
            labels.get(pos).remove((int)p.second);

            adapter.notifyItemRemoved(p.second);
            adapter.notifyItemRangeChanged(p.second, adapter.apps.size());
            String stringPackageName = p.first.packageName;
            //As of right now, doesn't work
//            Intent intent = new Intent(parent.getContext(), parent.getContext().getClass());
            Intent i = new Intent(Intent.ACTION_DELETE);
            i.setData(Uri.parse("package:"+stringPackageName));
            rv.getContext().startActivity(i);
//            PendingIntent sender = PendingIntent.getActivity(parent.getContext(), 0, intent, 0);
//            PackageInstaller mPackageInstaller = pm.getPackageInstaller();
//            mPackageInstaller.uninstall(p.first.packageName, sender.getIntentSender());
        }
        finishUninstalling(v);
    }

    //Reset delete mode and update all apps to reset
    public void finishUninstalling(View v){
        uninstallStack.clear();
        applyDeleteMode(false);
    }

    @Override
    public int getItemCount() {
        return keys.length;
    }
}
