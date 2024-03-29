package com.example.deepsee;



import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Context;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.os.Build;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.deepsee.contacts.Contact;
import com.example.deepsee.databinding.ActivityMainBinding;
import com.example.deepsee.messaging.SMSMessages;
import com.example.deepsee.messaging.SMSReader;
import com.example.deepsee.weather.WeatherListener;
import com.example.deepsee.weather.WeatherRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.example.deepsee.databinding.ActivityMainBinding;


import androidx.appcompat.app.AlertDialog;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import androidx.core.content.ContextCompat;

import androidx.navigation.ui.AppBarConfiguration;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import java.io.IOException;

import android.widget.Button;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.deepsee.messaging.SMSActivity;

import android.provider.ContactsContract;

import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import android.widget.ImageButton;



public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "Persistence";
    private boolean isAppDrawerVisible = false;
    public List<ApplicationInfo> apps;
    public HashMap<Integer, List<ApplicationInfo>> categories;

    private AppBarConfiguration appBarConfiguration;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    void sortAppCategories(){
        pm = getPackageManager();
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
        setContentView(R.layout.activity_main);

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

        ImageButton shortcutsButton = findViewById(R.id.shDrawerButton);

        sortAppCategories();


        // Persistent notification:
        showAlert();

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


        ArrayList<Contact> contacts;
        contacts = Contact.getContacts(MainActivity.this);
        SMSReader smsReader = new SMSReader();
        List<SMSMessages> smsMessages = smsReader.readSMS(MainActivity.this, contacts,5);
        SMSMessages_widget(smsMessages);


        handler = new Handler();
        handler.postDelayed(updateTask, 10000);
    }

    private boolean hasPermissions() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    // Creates a persistent Notification for the accessibility shortcut
    private void showAlert() {

        // Creates a Channel (Needed after API 26)
        CharSequence name = "Accessibility Shortcut";
        String description = "Persistent Notification for Accessibility Features.";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);


        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this.
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        //Quick Settings Notification
        Intent intent = new Intent(MainActivity.this, QuickSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // This generates the properties and Intent/Content for the notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Quick-Settings")
                .setContentText("Access Your Most Important Settings")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(451)  // Customise in Visual Overhaul
                .setOngoing(true)   // Persistence.
                .setContentIntent(pendingIntent);

        // This is a hacky fix that just DEPLOYS the notification.
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        nm.notify(0, builder.build());
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
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int displayName = cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME);

                if(id >= 0 && displayName >= 0){
                    String contactID = cursor.getString(id);
                    Cursor phoneNumCursor = getContentResolver()
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                    new String[] { contactID }, null);


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

    // TODO: CAUSING A CRASH ON RELOAD OF MAINACTIVITY. POSSIBLE DESYNC. LINE 259.
    // TODO: CRASH ONLY RESOLVED ON CLEARING APP STORAGE. COULD BE TIED TO "WHILE USING APP" PERMISSION
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
                                } catch (Exception e) {
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
    private void SMSMessages_widget(List<SMSMessages> smsMessages) {
        LinearLayout linearLayout = findViewById(R.id.Messages_widget_layout);
        linearLayout.removeAllViews();

        for (SMSMessages smsMessage : smsMessages) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.widget_msgs, null);
            TextView contactNameTextView = cardView.findViewById(R.id.contact_name);
            TextView timeTextView = cardView.findViewById(R.id.timeofmsg);
            TextView messageTextView = cardView.findViewById(R.id.textmsg_shortened);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String time = dateFormat.format(smsMessage.getTimestamp());

            contactNameTextView.setText(smsMessage.getContactName());
            timeTextView.setText(time);
            messageTextView.setText(smsMessage.getMessage());

            // Set layout parameters to make each card fill the layout
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            cardView.setLayoutParams(layoutParams);

            linearLayout.addView(cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("smsto:" + smsMessage.getContactName())); // Opens default messaging app with the specific contact
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTask);
    }


    @Override
    public void onBackPressed() {
        // Do nothing (override default back button behavior)
    }

}
