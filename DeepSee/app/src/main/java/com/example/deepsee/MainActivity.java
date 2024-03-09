package com.example.deepsee;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.deepsee.AppDrawerFragment;
import com.example.deepsee.R;
import com.example.deepsee.app_drawer.AppsAdapter;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepsee.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private boolean isAppDrawerVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showHideButton = findViewById(R.id.show_hide_button);
        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // TODO: fahiiiiiiiiiim jaani pls fragmentise
                //toggleAppDrawer(); // Temporarily disabled, see below launching activity:

                // Start AppDrawerActivity
                Intent intent = new Intent(MainActivity.this, AppDrawerActivity.class);
                startActivity(intent);
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
                    .replace(R.id.fragment_container, new AppDrawerFragment())
                    .commit();
        } else {
            // If it is visible, hide it:
            isAppDrawerVisible = !isAppDrawerVisible; // placeholder
        }

        isAppDrawerVisible = !isAppDrawerVisible;
    }

}
