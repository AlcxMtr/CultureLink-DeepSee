package com.example.deepsee;

import android.app.FragmentManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.deepsee.app_drawer.AppsAdapter;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.databinding.ActivityMainBinding;

import java.util.List;

public class AppDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Fragment app_drawer = new AppDrawerFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment_container, app_drawer);
//        transaction.commit();

        // Get installed Apps for RecyclerView:
        final PackageManager pm = getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(PackageManager.GET_META_DATA);

        // Filter out system apps from list:
        packs.removeIf(packageInfo ->
                (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);


        RecyclerView appRecyclerView = (RecyclerView) findViewById(R.id.apps_recycler);
        AppsAdapter adapter = new AppsAdapter(packs, pm);
        appRecyclerView.setAdapter(adapter);
        appRecyclerView.setLayoutManager(new GridLayoutManager( binding.getRoot().getContext(), 5));
    }
}