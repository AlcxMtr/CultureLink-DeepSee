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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.provider.ContactsContract;


public class MainActivity extends AppCompatActivity {

    private boolean isAppDrawerVisible = false;
    public List<ApplicationInfo> apps;
    public HashMap<Integer, List<ApplicationInfo>> categories;

    private AppBarConfiguration appBarConfiguration;
    Button btnSettings;
    private ActivityMainBinding binding;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                String[] projection = new String[]{
                        ContactsContract.Profile._ID,
                        ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
                        ContactsContract.Profile.LOOKUP_KEY,
                        ContactsContract.Profile.HAS_PHONE_NUMBER
                };
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                        int displayName = cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME);

                        if(id >= 0 && displayName >= 0){
                            String contactID = cursor.getString(id);
                            Cursor phoneNumCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { contactID }, null);

                            if (phoneNumCursor.moveToFirst()){
                                int num = phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                if(num >=0){
                                    System.out.println(cursor.getString(displayName) + ": " + phoneNumCursor.getString(num));
                                }
                            }
                            phoneNumCursor.close();
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } else {
                System.out.println("Woops...");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showHideButton = findViewById(R.id.show_hide_button);
        Button emergencyButton = findViewById(R.id.emergency_button);
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

        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                startActivity(new Intent(MainActivity.this, EmergencyActivity.class));
                transaction.commit();
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        });

        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);

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
