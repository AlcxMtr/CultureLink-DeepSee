package com.example.deepsee;
import android.Manifest;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.deepsee.messaging.SMSActivity;

public class MainActivity extends AppCompatActivity {

    private boolean isAppDrawerVisible = false;
    public List<PackageInfo> apps;
    public HashMap<Integer, List<PackageInfo>> categories;

    String [] permissions;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Read sms permission granted");
            } else {
                System.out.println("Read sms permission not granted");
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Contact permission granted");
            } else {
                System.out.println("Contact permission not granted");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissions = new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS
        };

        ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);

        Button showHideButton = findViewById(R.id.show_hide_button);
        Button messagesButton = findViewById(R.id.messagesbutton);
        final PackageManager pm = getPackageManager();

        // Get Package List:
        apps = pm.getInstalledPackages(PackageManager.GET_META_DATA);
//                apps.get(0).applicationInfo.category

        // Remove System Packages from the list before drawing:
        apps.removeIf(packageInfo ->
                (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);

        //Create dictionary from category number -> list of apps in said category
        categories = new HashMap<>();
        for (PackageInfo p : apps){
            if (!categories.containsKey(p.applicationInfo.category)){
                categories.put(p.applicationInfo.category,new ArrayList<PackageInfo>());
            }
            categories.get(p.applicationInfo.category).add(p);
        }

        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // TODO: fahiiiiiiiiiim jaani pls fragmentise

                // Start AppDrawerFragment
                Fragment fragment = new AppDrawerFragment(categories, apps, pm);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });

        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View k) {
                // Create an instance of the SMSReader class
                Intent msg_intent = new Intent(MainActivity.this, SMSActivity.class);
                startActivity(msg_intent);
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
