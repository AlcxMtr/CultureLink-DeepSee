package com.example.deepsee.app_drawer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.R;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class AppsAdapter extends RecyclerView.Adapter<AppContainer> {
    private List<ApplicationInfo> apps;
    private final PackageManager pm;
    private final Context con;
    private final List<Function<Boolean, Boolean>> toggleAppMode;
    private final Function<View, Boolean> longPressHandler;
    public AppsAdapter(@NonNull Context context, List<ApplicationInfo> apps, PackageManager pm, Function<View, Boolean> longPressHandler) {
        super();
        this.apps = apps;
        this.pm = pm;
        this.con = context;
        this.longPressHandler = longPressHandler;
        toggleAppMode = new LinkedList<>();
    }

    //Toggles behaviour and design of app container according to deleteMode
    private Boolean toggleAppDeleteMode(AppContainer v, boolean deleteMode, int pos, Function<View, Boolean> normalOnClickHandler){
        View icon = v.itemView.findViewById(R.id.delete_icon);
        if (deleteMode){
            v.itemView.setOnClickListener(view -> deleteModeOnClick(pos));
            icon.setVisibility(View.VISIBLE);
            /* Idea:
                App have some form of jiggle/animation to indicate deletion mode
                Have bar appear on the top to house undo-button, title bar saying delete, done button,
                and cancel button.
            * */
        }else{
            v.itemView.setOnClickListener(normalOnClickHandler::apply);
            icon.setVisibility(View.GONE);
        }
        return true;
    }

    //Iterate through all apps in category and toggle delete mode
    public Boolean setDeleteMode(boolean deleteMode){
        for (Function<Boolean, Boolean> f:toggleAppMode){
            f.apply(deleteMode);
        }
        return true;
    }

    //Uninstalls app held at position pos
    public void deleteModeOnClick(int pos){
        String appPackage = apps.get(pos).packageName;

        System.out.println("Attempted to uninstall: "+appPackage);
        //As of right now, doesn't work
//        Intent intent = new Intent(con, con.getClass());
//        PendingIntent sender = PendingIntent.getActivity(con, 0, intent, 0);
//        PackageInstaller mPackageInstaller = pm.getPackageInstaller();
//        mPackageInstaller.uninstall(appPackage, sender.getIntentSender());
        /*Idea:
        *   Instead of deleting apps right away, add apps to delete to a stack. Allow for
        *   undo button to pop App off of stack to undo deletion. When mode is toggled from
        *   delete-mode to normal, iterate pop items off of stack and uninstall them*/
    }

    /*
     * Creates AppContainer to hold one app square*/
    @NonNull
    @Override
    public AppContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(con);

        View app_square = inflater.inflate(R.layout.app_square, parent, false);

        AppContainer v = new AppContainer(app_square);
        v.con = con;
        return v;
    }

    /*
     * Bind app name, app icon, and app launch-intent to app square*/
    @Override
    public void onBindViewHolder(@NonNull AppContainer holder, int position) {
        ApplicationInfo p = apps.get(position);
        try {
            holder.icon.setImageDrawable(pm.getApplicationIcon(p.packageName));
            holder.name.setText(pm.getApplicationLabel(pm.getApplicationInfo(p.packageName,0)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(view -> launchApp(holder, p));
        holder.itemView.setOnLongClickListener(longPressHandler::apply);

        //Allows Adapter to iterate through all apps for delete mode
        toggleAppMode.add(x -> toggleAppDeleteMode(holder, x, position, view->launchApp(holder, p)));
    }

    private Boolean launchApp(AppContainer v, ApplicationInfo info){
        Intent launchIntent = new Intent(pm.getLaunchIntentForPackage(info.packageName));
        v.con.startActivity(launchIntent);
        return true;
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}
