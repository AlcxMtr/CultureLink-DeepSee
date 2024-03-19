package com.example.deepsee;

import static android.app.PendingIntent.getActivity;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {


    ListView lvSettings;
//    AlertDialog.Builder builder;

    int textSize =  -1;

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
                if(i == 0) {

                    boolean settingsPer = Settings.System.canWrite(SettingsActivity.this);
                    if(settingsPer){
                        //Log.d("ListView", "Item clicked: " + i);
                        String[] options = {"Small", "Medium", "Large"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle("Set Text Size");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int size) {
                                if (size == 0) {

                                    //SettingsActivity.this.setTheme((R.style.FontSizeSmall));
                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 0.5);


                                } else if(size == 1) {

                                    //SettingsActivity.this.setTheme((R.style.FontSizeMedium));
                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 1.0);

                                } else if(size == 2) {

                                    //SettingsActivity.this.setTheme((R.style.FontSizeLarge));
                                    Settings.System.putFloat(getBaseContext().getContentResolver(),
                                            Settings.System.FONT_SCALE, (float) 2.0);
                                }
                            }
                        });
                        builder.show();

                    }else{
                        Toast.makeText(SettingsActivity.this, "Please allow write permissions", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + SettingsActivity.this.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                } else if(i == 1) {

                    boolean settingsPer = Settings.System.canWrite(SettingsActivity.this);
                    if(settingsPer){
                        //Log.d("ListView", "Item clicked: " + i);
                        String[] options = {"Light Mode", "Dark Mode"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle("Set Theme");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int opt) {
                                if (opt == 0) {

                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                                } else if(opt == 1) {

                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                                }
                            }
                        });
                        builder.show();

                    }else{
                        Toast.makeText(SettingsActivity.this, "Please allow write permissions", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + SettingsActivity.this.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }

                }
            }
        });


    }

    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter() {
        ArrayList<String> setting_arr = new ArrayList<String>();
        setting_arr.add("Easy Viewing");
        setting_arr.add("Display Settings");
        setting_arr.add("Sound Volume");
        setting_arr.add("Privacy and Security");
        setting_arr.add("Night Mode");
        setting_arr.add("Notifications");
        setting_arr.add("Voice Commands");
        setting_arr.add("Language");


        ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, setting_arr);
        return array_adapter;
    }
}