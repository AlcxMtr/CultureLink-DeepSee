package com.example.deepsee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.deepsee.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Deepsee Settings");

        lv = (ListView) findViewById(R.id.lvSettings);

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
        lv.setAdapter(array_adapter);
    }
}