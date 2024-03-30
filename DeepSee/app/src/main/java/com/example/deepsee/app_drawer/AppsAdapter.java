package com.example.deepsee.app_drawer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
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

import com.example.deepsee.AppDrawerFragment;
import com.example.deepsee.MainActivity;
import com.example.deepsee.R;
import com.example.deepsee.auto_suggest.AlgoStruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class AppsAdapter extends RecyclerView.Adapter<AppContainer> {
    public final List<ApplicationInfo> apps;
    private final Context con;
    private final List<Consumer<Boolean>> toggleAppMode;

    //Prefetched data for view holders
    private final List<Drawable> icons;
    private final List<CharSequence> labels;
    private final List<Intent> launchers;
    private final Runnable longPressHandler;    //Lambda referring to AppDrawer toggle for delete mode
    private final ColorMatrixColorFilter gray;  //Color filter used to make apps in uninstall-stack
                                                //Look gray
    public AppsAdapter(@NonNull Context context, List<ApplicationInfo> apps, List<Drawable> icons, List<CharSequence> labels, List<Intent> launchers, Runnable longPressHandler) {
        super();
        this.apps = apps;
        this.con = context;
        this.longPressHandler = longPressHandler;
        this.icons = icons;
        this.labels = labels;
        this.launchers = launchers;
        toggleAppMode = new ArrayList<>(apps.size());
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        gray = new ColorMatrixColorFilter(cm);
    }

    /*Clear adapters lambda iterator (rebinds of the CategoryContainer parent
    / causes issues)*/
    public void clearToggleList(){
        toggleAppMode.clear();
    }
    //Toggles behaviour and design of app container according to deleteMode
    private void toggleAppDeleteMode(AppContainer v, boolean deleteMode){
        View icon = v.itemView.findViewById(R.id.delete_icon);
        if (deleteMode){
            v.itemView.setOnClickListener(view -> deleteModeOnClick(view, v.getAdapterPosition()));
            icon.setVisibility(VISIBLE);
            /* Idea:
                App have some form of jiggle/animation to indicate deletion mode
                Have bar appear on the top to house undo-button, title bar saying delete, done button,
                and cancel button.
            * */
        }else{
            v.itemView.setOnClickListener(view->launchApp(v, v.getAdapterPosition()));
            undoDelete(v.itemView);
            icon.setVisibility(GONE);
        }
    }

    //Iterate through all apps in category and toggle delete mode
    public void setDeleteMode(boolean deleteMode){
        for (Consumer<Boolean> f:toggleAppMode){
            f.accept(deleteMode);
        }
    }

    //Uninstalls app held at position pos
    public void deleteModeOnClick(View v, int pos){
        String appPackage = apps.get(pos).packageName;

        System.out.println("Attempted to uninstall: "+appPackage);
        CategoriesAdapter.uninstallStack.push(new Pair<>(apps.get(pos), pos));
        toDelete(v);
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

    //Set viewholder to be "open to delete" visually
    public void toDelete(View v){
        ((ImageView) v.findViewById(R.id.app_icon)).setColorFilter(gray);
        v.findViewById(R.id.delete_icon).setVisibility(GONE);
        v.setClickable(false);
    }

    //Set viewholder to be "in delete stack" visually
    public void undoDelete(View v){
        ((ImageView) v.findViewById(R.id.app_icon)).setColorFilter(null);
        v.findViewById(R.id.delete_icon).setVisibility(VISIBLE);
        v.setClickable(true);
    }

//    /*
//     * Bind app name, app icon, and app launch-intent to app square*/
//    @Override
//    public void onBindViewHolder(@NonNull AppContainer holder, int position) {
//        holder.icon.setImageDrawable(icons.get(position));
//        holder.name.setText(labels.get(position));
//        holder.icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent launchIntent = new Intent(pm.getLaunchIntentForPackage(p.packageName));
//                // TODO: THIS IS WHERE WE NOTIFY ALEX'S ALGORITHM
//                holder.con.startActivity(launchIntent);
//            }
//        });
//
//        toggleAppMode.add(x -> toggleAppDeleteMode(holder, x));
//        toggleAppDeleteMode(holder, AppDrawerFragment.deleteMode);
//    }

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

    //Launches app represented by pos
    private void launchApp(AppContainer v, int pos){
        MainActivity.reccomender.appOpened(apps.get(pos).packageName);
//        MainActivity.storageManager.syncStorageManage();
        Intent launchIntent = launchers.get(pos);
        v.con.startActivity(launchIntent);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}
