package com.example.deepsee;
import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.deepsee.accessibility.TextAndSpeech;
import com.example.deepsee.databinding.ActivityMainBinding;
import com.example.deepsee.weather.WeatherListener;
import com.example.deepsee.weather.WeatherRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.provider.ContactsContract;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private boolean isAppDrawerVisible = false;
    public List<ApplicationInfo> apps;
    public HashMap<Integer, List<ApplicationInfo>> categories;

    private AppBarConfiguration appBarConfiguration;
    Button btnSettings;
    private ActivityMainBinding binding;

    private TextView weather_location;
    private TextView temp;
    private TextView rain_chance;
    private TextView humid;
    private TextView wind_speed;

    private TextView cond;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private String[] permissions;

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

        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };

//        if (!hasPermissions()) {
//            ActivityCompat.requestPermissions(MainActivity.this, permissions, 200);
//        }

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
        for (ApplicationInfo p : apps) {
            if (!categories.containsKey(p.category)) {
                categories.put(p.category, new ArrayList<ApplicationInfo>());
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

        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);

        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EmergencyContactFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
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

        weather_location = (TextView) findViewById(R.id.weather_location);
        temp = (TextView) findViewById(R.id.temperature);
        rain_chance = (TextView) findViewById(R.id.rain_chance);
        wind_speed = (TextView) findViewById(R.id.wind_speed);
        humid = (TextView) findViewById(R.id.humidity);
        cond = (TextView) findViewById(R.id.condition);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
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
                System.out.print("Error retrieving weather");
            }
        };
        WeatherRequest.getWeatherInfo(weatherListener, latitude, longitude);
    }

    private boolean hasPermissions() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
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
