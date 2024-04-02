package com.example.deepsee;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.app_drawer.AppContainer;
import com.example.deepsee.app_drawer.CategoriesAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// Class that holds a list of ShortcutContainer & Launchables for use by ShortcutContrainerFragment.
// Handles pretty much purely internal activities.
public class ShortcutsAdapter extends RecyclerView.Adapter<AppContainer> {
    private final Context con;
    private final List<ApplicationInfo> apps;
    //Prefetched data for view holders
    private final List<Drawable> icons;
    private final List<CharSequence> labels;
    private final List<Intent> launchers;
    //Look gray
    public ShortcutsAdapter(@NonNull Context context, List<ApplicationInfo> allApps, PackageManager pm) {
        super();
        this.con = context;
        this.icons = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.launchers = new ArrayList<>();
        String[] names = MainActivity.reccomender.mostRelevant(5);
        apps = allApps.stream().filter(x -> Arrays.stream(names).anyMatch(
                s -> x.packageName.equals(s)))
                .collect(Collectors.toList());

        for (ApplicationInfo info:apps){
            try {
                icons.add(pm.getApplicationIcon(info.packageName));
            } catch (PackageManager.NameNotFoundException e) {
                System.out.println("Icon not found for "+info.packageName+"...");
            }
            try {
                labels.add(pm.getApplicationLabel(pm.getApplicationInfo(info.packageName,0)));
            } catch (PackageManager.NameNotFoundException e) {
                System.out.println("Label not found for "+info.packageName+"...");
            }
            launchers.add(new Intent(pm.getLaunchIntentForPackage(info.packageName)));
        }
    }
    /*
     * Creates AppContainer to hold one app square*/
    @NonNull
    @Override
    public AppContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(con);

        View app_square = inflater.inflate(R.layout.app_square, parent, false);

        AppContainer v = new AppContainer(app_square);
        //Allows Adapter to iterate through all apps for delete mode
        v.con = con;
        return v;
    }

    //Set viewholder to be "in delete stack" visually

    /*
     * Bind app name, app icon, and app launch-intent to app square*/
    @Override
    public void onBindViewHolder(@NonNull AppContainer holder, int position) {
        holder.icon.setImageDrawable(icons.get(position));
        holder.name.setText(labels.get(position));

        holder.itemView.setOnClickListener(view -> launchApp(holder, position));
    }

    //Launches app represented by pos
    private void launchApp(AppContainer v, int pos){
        MainActivity.reccomender.appOpened(apps.get(pos).packageName);
        System.out.println("App open tracked: " + apps.get(pos).packageName);

        Intent launchIntent = launchers.get(pos);
        v.con.startActivity(launchIntent);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}