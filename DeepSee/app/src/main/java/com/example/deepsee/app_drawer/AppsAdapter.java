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
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class AppsAdapter extends RecyclerView.Adapter<AppContainer> {
    private final List<ApplicationInfo> apps;
    private final Context con;
    private final List<Consumer<Boolean>> toggleAppMode;
    private final List<Drawable> icons;
    private final List<CharSequence> labels;
    private final List<Intent> launchers;
    private final Runnable longPressHandler;
    public AppsAdapter(@NonNull Context context, List<ApplicationInfo> apps, List<Drawable> icons, List<CharSequence> labels, List<Intent> launchers, Runnable longPressHandler) {
        super();
        this.apps = apps;
        this.con = context;
        this.longPressHandler = longPressHandler;
        this.icons = icons;
        this.labels = labels;
        this.launchers = launchers;
        toggleAppMode = new ArrayList<>(apps.size());
    }



    public void clearToggleList(){
        toggleAppMode.clear();
    }
    //Toggles behaviour and design of app container according to deleteMode
    private void toggleAppDeleteMode(AppContainer v, boolean deleteMode){
        View icon = v.itemView.findViewById(R.id.delete_icon);
        if (deleteMode){
            v.itemView.setOnClickListener(view -> deleteModeOnClick(v.getAdapterPosition()));
            icon.setVisibility(View.VISIBLE);
            /* Idea:
                App have some form of jiggle/animation to indicate deletion mode
                Have bar appear on the top to house undo-button, title bar saying delete, done button,
                and cancel button.
            * */
        }else{
            v.itemView.setOnClickListener(view->launchApp(v, v.getAdapterPosition()));
            icon.setVisibility(View.GONE);
        }
    }

    //Iterate through all apps in category and toggle delete mode
    public void setDeleteMode(boolean deleteMode){
        for (Consumer<Boolean> f:toggleAppMode){
            f.accept(deleteMode);
        }
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
        //Allows Adapter to iterate through all apps for delete mode

        toggleAppDeleteMode(v, AppDrawerFragment.deleteMode);
        v.con = con;
        return v;
    }

    /*
     * Bind app name, app icon, and app launch-intent to app square*/
    @Override
    public void onBindViewHolder(@NonNull AppContainer holder, int position) {
        holder.icon.setImageDrawable(icons.get(position));
        holder.name.setText(labels.get(position));

        holder.itemView.setOnClickListener(view -> launchApp(holder, position));
        holder.itemView.setOnLongClickListener(view -> {
            longPressHandler.run();
            return true;
        });

        toggleAppMode.add(x -> toggleAppDeleteMode(holder, x));
        toggleAppDeleteMode(holder, AppDrawerFragment.deleteMode);
    }

    private Boolean launchApp(AppContainer v, int pos){
        Intent launchIntent = launchers.get(pos);
        v.con.startActivity(launchIntent);
        return true;
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}
