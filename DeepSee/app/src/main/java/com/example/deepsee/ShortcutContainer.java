package com.example.deepsee;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.app_drawer.AppContainer;

// Class for use by ShortcutsFragment and others that need an AppContainer with a builtin
// Launch/Activity intent. This allows us to generalise from app packages w/ Launch intents to
// any activity, internal or external. TODO: Mohammad, you may want this. Or something similar.
public class ShortcutContainer extends RecyclerView.ViewHolder {
    public ImageView icon;
    public Context con;
    public Intent shortcutIntent;


    public ShortcutContainer(@NonNull View itemView, ImageView icon, Intent intent) {
        super(itemView);
        this.icon = icon;
        this.shortcutIntent = intent;
    }
}
