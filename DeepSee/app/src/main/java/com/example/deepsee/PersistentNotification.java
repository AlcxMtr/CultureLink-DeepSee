package com.example.deepsee;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class PersistentNotification extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "my_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification() {
        // Create notification intent
        // TODO: Akshath, this is where you put your intent.
        Intent intent = new Intent(this, AppDrawerFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.persistent_notification)   // TODO: fix drawable
                .setContentTitle("Persistent Notification")
                .setContentText("akshath!!!")
                .setOngoing(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel for Android Oreo and above
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Accessibility Shortcut",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        // Show notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
