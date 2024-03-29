package com.example.deepsee;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.example.deepsee.messaging.SMSActivity;


public class ShortcutsDrawerActivity extends Activity {

    private ImageButton conversationsButton;
    private ImageButton assistantButton;
    private ImageButton emcButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortcuts_drawer_activity);

        conversationsButton = findViewById(R.id.Conversations);
        assistantButton = findViewById(R.id.AssistantButton);
        emcButton = findViewById(R.id.EMCButton);
        settingsButton = findViewById(R.id.AKSettings);

        //TODO:  Takes us to Mohammad's Conversations Activity:
        conversationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msg_intent = new Intent(ShortcutsDrawerActivity.this, SMSActivity.class);
                startActivity(msg_intent);
            }
        });

        //TODO: Takes us to Google Assistant's Activity:
        assistantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://assistant.google.com"));
                startActivity(intent);
            }
        });

        //TODO: Takes us to Angel's Emergency Contacts
        emcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShortcutsDrawerActivity.this, EmrgActivity.class);
                startActivity(intent);
            }
        });

        //TODO: Takes us to Akshath's Settings Activity:
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShortcutsDrawerActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    // TODO: NEED DRAWABLES
    // Closes this activity when touched:
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle taps outside of ImageButtons (equivalent to pressing back button)
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onBackPressed();
        }
        return true;
    }
}
