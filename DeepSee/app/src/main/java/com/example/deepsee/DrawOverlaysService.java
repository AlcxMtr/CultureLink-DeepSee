package com.example.deepsee;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class DrawOverlaysService extends Service {

    private Class<? extends Activity> activityClass;
    private WindowManager windowManager;
    private View overlayView;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get the class of the activity from the intent
        activityClass = (Class<? extends Activity>) intent.getSerializableExtra("activityClass");

        // Inflate the layout based on the activity class
        if (activityClass == TranslationActivity.class) {
            overlayView = LayoutInflater.from(this).inflate(R.layout.activity_translation, null);
        } else if (activityClass == QuickSettingsActivity.class) {
            overlayView = LayoutInflater.from(this).inflate(R.layout.activity_quick_settings, null);
        }

        // Adjust layout parameters for the overlay view
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        // Set position of the overlay view, for example:
        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = 0;
        layoutParams.y = 100; // Adjust as needed

        // Get WindowManager service
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Add the overlay view to the window manager
        windowManager.addView(overlayView, layoutParams);
        
        // Return START_STICKY to ensure the service is restarted if it's killed by the system
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null) {
            windowManager.removeView(overlayView);
            overlayView = null;
        }
    }
}
