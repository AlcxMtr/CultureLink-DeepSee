package com.example.deepsee;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Button;
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

public class QuickSettingsActivity extends AppCompatActivity {


    ListView lvQuickSettings;
    Button btnDone;

    int textSize = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_settings);

//        getSupportActionBar().setTitle("Deepsee Settings");

        lvQuickSettings = (ListView) findViewById(R.id.lvQuickSettings);

        btnDone = (Button) findViewById(R.id.btnDone);

        ArrayAdapter<String> array_adapter = getStringArrayAdapter();
        lvQuickSettings.setAdapter(array_adapter);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuickSettingsActivity.this.finish();
            }
        });

        lvQuickSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    boolean settingsPer = Settings.System.canWrite(QuickSettingsActivity.this);
                    if (settingsPer) {
                        //Log.d("ListView", "Item clicked: " + i);
                        String[] options = {"Small", "Medium", "Large"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(QuickSettingsActivity.this);
                        builder.setTitle("Set Text Size");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int size) {
                                if (size == 0) {

                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 0.5);


                                } else if (size == 1) {

                                    //SettingsActivity.this.setTheme((R.style.FontSizeMedium));
                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 1.0);

                                } else if (size == 2) {

                                    //SettingsActivity.this.setTheme((R.style.FontSizeLarge));
                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 2.0);
                                }
                            }
                        });
                        builder.show();

                    } else {
                        Toast.makeText(QuickSettingsActivity.this, "Please allow write permissions", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + QuickSettingsActivity.this.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                } else if (i == 1) {

                    boolean settingsPer = Settings.System.canWrite(QuickSettingsActivity.this);
                    if (settingsPer) {
                        //Log.d("ListView", "Item clicked: " + i);
                        String[] options = {"Light Mode", "Dark Mode"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(QuickSettingsActivity.this);
                        builder.setTitle("Set Theme");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int opt) {
                                if (opt == 0) {

                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                                } else if (opt == 1) {

                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                                }
                            }
                        });
                        builder.show();

                    } else {
                        Toast.makeText(QuickSettingsActivity.this, "Please allow write permissions", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + QuickSettingsActivity.this.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }

                } else if (i == 2) {

                    Toast.makeText(QuickSettingsActivity.this, "Jello", Toast.LENGTH_LONG).show();


                }

            }

        });


    }

    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter() {
        ArrayList<String> setting_arr = new ArrayList<String>();
        setting_arr.add("Easy Viewing");
        setting_arr.add("Display Settings");
        setting_arr.add("Language");
        setting_arr.add("Bluetooth");
        setting_arr.add("Night Mode");
        setting_arr.add("Notifications");
        setting_arr.add("Voice Commands");
        setting_arr.add("Language");


        ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, setting_arr);
        return array_adapter;
    }
}