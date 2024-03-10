package com.example.deepsee.app_drawer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{
    private List<PackageInfo> apps;
    private PackageManager pm;

    public AppsAdapter(List<PackageInfo> apps, PackageManager pm) {
        this.apps = apps;
        this.pm = pm;
    }

    /*
    * Creates view for whole recycler*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View appSquare = inflater.inflate(R.layout.app_square, parent, false);
        ViewHolder v = new ViewHolder(appSquare);
        v.con = context;
        return v;
    }

    /*
    * Binds data to one View in list of Views*/
    @Override
    public void onBindViewHolder(@NonNull AppsAdapter.ViewHolder holder, int position) {
        PackageInfo p = apps.get(position);
        try {
            holder.icon.setImageDrawable(pm.getApplicationIcon(p.packageName));
        } catch (PackageManager.NameNotFoundException e) {
        }

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(pm.getLaunchIntentForPackage(p.packageName));
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

    /*
    * Data structure that holds View for one item*/
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView icon;
        public TextView name;
        public Context con;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.app_icon);
            name = (TextView) itemView.findViewById(R.id.app_name);
        }
    }
}
