package com.example.deepsee.app_drawer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppContainer> {
    private List<ApplicationInfo> apps;
    private PackageManager pm;
    public AppsAdapter(@NonNull Context context, List<ApplicationInfo> apps, PackageManager pm) {
        super();
        this.apps = apps;
        this.pm = pm;
    }

    /*
     * Creates AppContainer to hold one app square*/
    @NonNull
    @Override
    public AppContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View app_square = inflater.inflate(R.layout.app_square, parent, false);

        AppContainer v = new AppContainer(app_square);
        v.con = context;
        return v;
    }

    /*
     * Bind app name, app icon, and app launch-intent to app square*/
    @Override
    public void onBindViewHolder(@NonNull AppContainer holder, int position) {
        ApplicationInfo p = apps.get(position);
        try {
            holder.icon.setImageDrawable(pm.getApplicationIcon(p.packageName));
        } catch (PackageManager.NameNotFoundException e) {
        }

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(pm.getLaunchIntentForPackage(p.packageName));
                // TODO: THIS IS WHERE WE NOTIFY ALEX'S ALGORITHM
                holder.con.startActivity(launchIntent);
            }
        });

        try {
            holder.name.setText(pm.getApplicationLabel(pm.getApplicationInfo(p.packageName,0)));
        } catch (PackageManager.NameNotFoundException e) {
            holder.name.setText("Oops");
        }
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}
