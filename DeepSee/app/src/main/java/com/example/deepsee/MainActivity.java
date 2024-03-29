package com.example.deepsee;
import android.Manifest;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;


import com.example.deepsee.accessibility.TextAndSpeech;
import com.example.deepsee.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.navigation.ui.AppBarConfiguration;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.deepsee.messaging.SMSActivity;

import android.provider.ContactsContract;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private boolean isAppDrawerVisible = false;
    public List<ApplicationInfo> apps;
    public HashMap<Integer, List<ApplicationInfo>> categories;

    private AppBarConfiguration appBarConfiguration;
    Button btnSettings;
    private ActivityMainBinding binding;
    private ShortcutsContainerFragment shortcutsFragment;

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
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
        };

        ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);

        Button showHideButton = findViewById(R.id.show_hide_button);

        Button messagesButton = findViewById(R.id.messagesbutton);

        Button emergencyButton = findViewById(R.id.emergency_button);
        ImageButton shortcutsButton = findViewById(R.id.shDrawerButton);
        Button shortcutsContainerButton = findViewById(R.id.shortcutsContainerButton);

        final PackageManager pm = getPackageManager();
        // Get Package List:
        apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

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
            public void onClick(View v) {

                // Start AppDrawerFragment
                Fragment fragment = new AppDrawerFragment(categories, apps, pm);

                //Draw AppDrawerFragment overtop current view
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


        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EmergencyContactFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
        List<ShortcutContainer> launchables = new ArrayList<>();

        shortcutsFragment = new ShortcutsContainerFragment(launchables, pm);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.shortcuts_holder, shortcutsFragment);
        transaction.commit();
        shortcutsContainerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shortcutsFragment.toggleVisibility();
            }
        });

        Button t2s = findViewById(R.id.t2s_button);
        t2s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TextAndSpeech.class);
                startActivity(i);
            }
        });

        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        shortcutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShortcutsDrawerActivity.class));
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
