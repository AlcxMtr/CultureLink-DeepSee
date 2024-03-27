package com.example.deepsee;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class SignalHandler extends Service {

    // This handles startup stuff:
    private BroadcastReceiver bootReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                // Start this app on boot
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("your.package.name");
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(launchIntent);
                }
            }
        }
    };

    // Runs when a package/app is added or replaced.
    private BroadcastReceiver packageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction()) ||
                    Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
                // A new app is installed or an existing app is updated
                String packageName = intent.getData().getEncodedSchemeSpecificPart();
                System.out.println("We just found out a package was installed or replaced.");
                //TODO: FAHIIIIIIIIM, ALEEEEEX:




            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // Register boot receiver
        registerReceiver(bootReceiver, new IntentFilter(Intent.ACTION_BOOT_COMPLETED));
        // Register package receiver
        IntentFilter packageFilter = new IntentFilter();
        packageFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        packageFilter.addDataScheme("package");
        registerReceiver(packageReceiver, packageFilter);
        System.out.println("Registered Signal Handlers!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister receivers
        unregisterReceiver(bootReceiver);
        unregisterReceiver(packageReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

