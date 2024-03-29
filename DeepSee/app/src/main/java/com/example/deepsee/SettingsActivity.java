package com.example.deepsee;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {


    ListView lvSettings;
    AlertDialog.Builder builder;

    int textSize = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        getSupportActionBar().setTitle("Deepsee Settings");

        lvSettings = (ListView) findViewById(R.id.lvSettings);

        ArrayAdapter<String> array_adapter = getStringArrayAdapter();
        lvSettings.setAdapter(array_adapter);

        lvSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    boolean settingsPer = Settings.System.canWrite(SettingsActivity.this);
                    if (settingsPer) {
                        //Log.d("ListView", "Item clicked: " + i);
                        String[] options = {"Small", "Medium", "Large"};
                        builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle("Set Text Size");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int size) {
                                if (size == 0) {

                                    //SettingsActivity.this.setTheme((R.style.FontSizeSmall));
                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 0.8);


                                } else if (size == 1) {

                                    //SettingsActivity.this.setTheme((R.style.FontSizeMedium));
                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 1.0);

                                } else if (size == 2) {

                                    //SettingsActivity.this.setTheme((R.style.FontSizeLarge));
                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 1.5);
                                }
                            }
                        });
                        builder.show();

                    } else {
                        Toast.makeText(SettingsActivity.this, "Please allow write permissions", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + SettingsActivity.this.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                } else if (i == 1) {


                    Intent intent = new Intent();
                    intent.setAction("android.settings.DISPLAY_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // for Android 8 and above
                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

                    startActivity(intent);


                } else if (i == 2) {

                    Intent intent = new Intent();
                    intent.setAction("android.settings.WIRELESS_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // for Android 8 and above
                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

                    startActivity(intent);
                } else if (i == 3) {
                    startActivityForResult(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS), 0);
                } else if (i == 4) {
                    startActivityForResult(new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS), 0);
                } else if(i == 5) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCALE_SETTINGS), 0);
                } else if(i == 6) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.ALL_APPS_NOTIFICATION_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

                    startActivity(intent);
                } else if(i == 7) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }

            }

        });


        // Enable drawing over other apps
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            // TODO: REQUEST PERMISSION FOR DRAWING OVER OTHER APPS!!
        } else {
            // Start the service to draw over other apps
            startDrawOverlaysService();
        }


    }

    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter() {
        ArrayList<String> setting_arr = new ArrayList<String>();
        setting_arr.add("Text Size");
        setting_arr.add("Display Settings");
        setting_arr.add("Network & Internet");
        setting_arr.add("Bluetooth");
        setting_arr.add("Battery");
        setting_arr.add("Language");
        setting_arr.add("Notifications");
        setting_arr.add("All Settings");



        ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, setting_arr);
        return array_adapter;
    }


    private void startDrawOverlaysService() {
        // Start the service to draw over other apps
        Intent serviceIntent = new Intent(this, DrawOverlaysService.class);
        serviceIntent.putExtra("activityClass", SettingsActivity.class);
        startService(serviceIntent);
    }

}