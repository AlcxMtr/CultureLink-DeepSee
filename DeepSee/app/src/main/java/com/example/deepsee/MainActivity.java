package com.example.deepsee;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isAppDrawerVisible = false;
    public List<PackageInfo> apps;
    public HashMap<Integer, List<PackageInfo>> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showHideButton = findViewById(R.id.show_hide_button);
        Button messagesButton = findViewById(R.id.messagesbutton);
        final PackageManager pm = getPackageManager();

        // Get Package List:
        apps = pm.getInstalledPackages(PackageManager.GET_META_DATA);
//                apps.get(0).applicationInfo.category

        // Remove System Packages from the list before drawing:
        apps.removeIf(packageInfo ->
                (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);

        //Create dictionary from category number -> list of apps in said category
        categories = new HashMap<>();
        for (PackageInfo p : apps){
            if (!categories.containsKey(p.applicationInfo.category)){
                categories.put(p.applicationInfo.category,new ArrayList<PackageInfo>());
            }
            categories.get(p.applicationInfo.category).add(p);
        }

        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // TODO: fahiiiiiiiiiim jaani pls fragmentise

                // Start AppDrawerFragment
                Fragment fragment = new AppDrawerFragment(categories, apps, pm);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });

        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View k) {
                // Create an instance of the SMSReader class
                SMSReader smsReader = new SMSReader();
//                // Call the readSMS method with the context of the MainActivity
                smsReader.readSMS(MainActivity.this);
                setContentView(R.layout.message);
//                SMSAdapter adapter = new SMSAdapter(MainActivity.this, R.layout.messages,smsReader.readSMS(MainActivity.this));
                List<SMSMessages> smsMessages = smsReader.readSMS(MainActivity.this);

                // Display SMS messages
                displaySMSMessages(smsMessages);

            }
        });

    }



    private void displaySMSMessages(List<SMSMessages> smsMessages) {
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        for (SMSMessages smsMessage : smsMessages) {
            // Inflate the card_sms_message layout
            View cardView = LayoutInflater.from(this).inflate(R.layout.layout_msgs, null);

            // Get references to the TextViews inside the CardView
            TextView contactNameTextView = cardView.findViewById(R.id.contact_name);
            TextView timeTextView = cardView.findViewById(R.id.timeofmsg);
            TextView messageTextView = cardView.findViewById(R.id.textmsg_shortened);

            // Set the text for each TextView
            contactNameTextView.setText(smsMessage.getContactName());
            timeTextView.setText(smsMessage.getTimestamp());
            messageTextView.setText(smsMessage.getMessage());

            // Add the CardView to the LinearLayout
            linearLayout.addView(cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an intent to open the messages app
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                    startActivity(intent);
                }
            });
    }}




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
