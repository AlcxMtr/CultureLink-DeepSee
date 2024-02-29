package com.example.deepsee;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.deepsee.app_drawer.AppsAdapter;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        RecyclerView appRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        AppsAdapter adapter = new AppsAdapter(pm.getInstalledPackages(PackageManager.GET_META_DATA), pm);
        appRecyclerView.setAdapter(adapter);
        appRecyclerView.setLayoutManager(new GridLayoutManager( binding.getRoot().getContext(), 5));
    }
}