package com.example.deepsee;
import static java.util.concurrent.TimeUnit.HOURS;

import android.Manifest;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.deepsee.accessibility.TextAndSpeech;
import com.example.deepsee.auto_suggest.AlgoStruct;
import com.example.deepsee.databinding.ActivityMainBinding;
import com.example.deepsee.weather.WeatherListener;
import com.example.deepsee.weather.WeatherRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import java.io.IOException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.example.deepsee.messaging.SMSActivity;

import android.provider.ContactsContract;

import android.widget.TextView;

import android.widget.ImageButton;



public class MainActivity extends AppCompatActivity {

    private boolean isAppDrawerVisible = false;
    public List<ApplicationInfo> apps;
    public HashMap<Integer, List<ApplicationInfo>> categories;

    private AppBarConfiguration appBarConfiguration;
    Button btnSettings;
    private ActivityMainBinding binding;
    PackageManager pm;

    private ShortcutsContainerFragment shortcutsFragment;

    private TextView weather_location;
    private TextView temp;
    private TextView rain_chance;
    private TextView humid;
    private TextView wind_speed;

    private TextView cond;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private RequestQueue requestQueue;

    private String[] permissions;

    private Handler handler;

    public static AlgoStruct reccomender;
    public static StorageManager storageManager;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//         if (requestCode == 1) {
//             if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                 System.out.println("Location permission granted");
//             } else {
//                 System.out.println("Location permission not granted");
//             }

//             if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                 System.out.println("Contact permission granted");
//                 getContacts();
//             } else {
//                 System.out.println("Contact permission not granted");
//             }
//         }
    }
    void sortAppCategories(){
        // Get Package List:
        apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        // Remove System Packages from the list before drawing:
        apps.removeIf(packageInfo ->
                (pm.getLaunchIntentForPackage(packageInfo.packageName) == null));

        //Create dictionary from category number -> list of apps in said category
        categories = new HashMap<>();
        for (ApplicationInfo p : apps) {
            if (!categories.containsKey(p.category)) {
                categories.put(p.category, new ArrayList<ApplicationInfo>());
            }
            categories.get(p.category).add(p);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pm = getPackageManager();

        setContentView(R.layout.activity_main);
        initializeRecommender();

        permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.WRITE_CONTACTS
        };

        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }


        ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);


        Button showHideButton = findViewById(R.id.show_hide_button);

        Button messagesButton = findViewById(R.id.messagesbutton);

        Button emergencyButton = findViewById(R.id.emergency_button);
        ImageButton shortcutsButton = findViewById(R.id.shDrawerButton);
        Button shortcutsContainerButton = findViewById(R.id.shortcutsContainerButton);
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

//        shortcutsFragment = new ShortcutsContainerFragment(launchables, pm);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.shortcuts_holder, shortcutsFragment);
//        transaction.commit();
//        shortcutsContainerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                shortcutsFragment.toggleVisibility();
//            }
//        });

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


        weather_location = (TextView) findViewById(R.id.weather_location);
        temp = (TextView) findViewById(R.id.temperature);
        rain_chance = (TextView) findViewById(R.id.rain_chance);
        wind_speed = (TextView) findViewById(R.id.wind_speed);
        humid = (TextView) findViewById(R.id.humidity);
        cond = (TextView) findViewById(R.id.condition);
        requestQueue = Volley.newRequestQueue(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        handler = new Handler();
        handler.postDelayed(updateTask, 10000);
    }

    /*TODO
    *  Tie storage manager and app-recommender together, and initialize them on app launch.*/
    private void initializeRecommender() {
        storageManager = new StorageManager(getBaseContext());
        reccomender = storageManager.getAlgoStruct();
        if (reccomender == null){
            reccomender = new AlgoStruct();
            storageManager.setAlgoStruct(reccomender);
            System.out.println("Created recommender file.");
        }
        apps = storageManager.getApps();
        categories = storageManager.getCategories();
        if (apps == null || categories == null){
            sortAppCategories();
            storageManager.setApps(apps);
            storageManager.setCategories(categories);
            System.out.println("Created apps and categories file.");
        }

        SynchronizingWork.addTask(storageManager::syncStorageManage);

        PeriodicWorkRequest wr = new PeriodicWorkRequest.Builder(
                SynchronizingWork.class, 16, TimeUnit.MINUTES).build();


        WorkManager.getInstance(getBaseContext()).enqueueUniquePeriodicWork(
                "Synchronizing app", ExistingPeriodicWorkPolicy.KEEP, wr);

        RecyclerView recommendedApp = findViewById(R.id.recommended_app_recycler);
        ShortcutsAdapter adapter = new ShortcutsAdapter(getBaseContext(), apps, pm);
        recommendedApp.setAdapter(adapter);
        recommendedApp.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private boolean hasPermissions() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            getLastLocation();
            handler.postDelayed(this, 10000);
        }
    };

    private void getContacts() {

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
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder
                                            .getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    String loc = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
                                    weather_location.setText(loc);
                                    getWeather(location.getLatitude(), location.getLongitude());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                weather_location.setText("Mountain View, United States");
                                getWeather(37.4220936, -122.083922);
                            }
                        }
                    });
        } else {
            weather_location.setText("Mountain View, United States");
            getWeather(37.4220936, -122.083922);
        }

    }

    private void getWeather(double latitude, double longitude) {
        WeatherListener weatherListener = new WeatherListener() {
            @Override
            public void onWeatherReceived(int temperature, int rainChance, int humidity, int windSpeed, String condition) {
                temp.setText(temperature + "°C");
                rain_chance.setText(rainChance + "%");
                humid.setText(humidity + "%");
                wind_speed.setText(windSpeed + " kph");
                cond.setText(condition);
            }

            @Override
            public void onWeatherError(String errorMessage) {
                System.out.println("Error retrieving weather");
            }
        };
        WeatherRequest.getWeatherInfo(requestQueue, weatherListener, latitude, longitude);

    }

    public void startDrawer(View v){
        // Start AppDrawerFragment
        Fragment fragment = new AppDrawerFragment(categories, apps, pm);

        //Draw AppDrawerFragment overtop current view
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTask);
    }
}
