package com.example.deepsee;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private boolean isAppDrawerVisible = false;
    public List<ApplicationInfo> apps;
    public HashMap<Integer, List<ApplicationInfo>> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showHideButton = findViewById(R.id.show_hide_button);
        final PackageManager pm = getPackageManager();

        // Get Package List:
        apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//                apps.get(0).applicationInfo.category

        // Remove System Packages from the list before drawing:

        apps.removeIf(packageInfo ->
                (pm.getLaunchIntentForPackage(packageInfo.packageName) == null));

        //Create dictionary from category number -> list of apps in said category
        categories = new HashMap<>();
        for (ApplicationInfo p : apps){
            if (!categories.containsKey(p.category)){
                categories.put(p.category,new ArrayList<ApplicationInfo>());
            }
            categories.get(p.category).add(p);
        }

        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // TODO: fahiiiiiiiiiim jaani pls fragmentise

                // Start AppDrawerFragment
                Fragment fragment = new AppDrawerFragment(categories, apps, pm);

                //Draw AppDrawerFragment overtop current view
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
    }

    // Toggles the App Drawer:
    private void toggleAppDrawer() {

        if (!isAppDrawerVisible) {
            // If the App Drawer is not visible, show it

            // TODO: THIS DOES NOT WORK JAANI FIX IT ILY
            // supportFragmentManager is an inbuilt, we should be using it:
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AppDrawerFragment(categories, apps, getPackageManager()))
                    .commit();
        } else {
            // If it is visible, hide it:
            isAppDrawerVisible = !isAppDrawerVisible; // placeholder
        }

        isAppDrawerVisible = !isAppDrawerVisible;
    }

}
