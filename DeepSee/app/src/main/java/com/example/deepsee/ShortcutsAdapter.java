package com.example.deepsee;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.app_drawer.AppContainer;

import java.util.ArrayList;
import java.util.List;

// Class that holds a list of ShortcutContainer & Launchables for use by ShortcutContrainerFragment.
// Handles pretty much purely internal activities.
public class ShortcutsAdapter extends RecyclerView.Adapter<ShortcutContainer> {
    private List<ShortcutContainer> launchables;
    public ShortcutsAdapter() {
        super();
        this.launchables = getLaunchables ();
    }

    private List<ShortcutContainer> getLaunchables() {
        // Fill out here the hardcoded list; you want to fill in your icon and push it to the list:
        List<ShortcutContainer> launches;
        // Internal Settings:
        if (launchables == null)
            launchables = new ArrayList<ShortcutContainer>();

        return launchables;
    }

    /*
     * Creates AppContainer to hold one app square*/
    @NonNull
    @Override
    public ShortcutContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View app_square = inflater.inflate(R.layout.app_square, parent, false);

        ShortcutContainer v = new ShortcutContainer(app_square, null, null);
        v.con = context;
        return v;
    }

    /*
     * Bind app name, app icon, and app launch-intent to app square*/
    @Override
    public void onBindViewHolder(@NonNull ShortcutContainer holder, int position) {

        // Binding icon
        ShortcutContainer p = launchables.get(position);
        // No longer necessary to bind Icon. Should be instantiated first.

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = p.shortcutIntent;
                holder.con.startActivity(launchIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return launchables.size();
    }
}