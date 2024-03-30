package com.example.deepsee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BroadcastReader extends BroadcastReceiver {

    private StorageManager sm;

    public BroadcastReader(StorageManager sm) {
        this.sm = sm;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sm.updateStorageManager();
    }
}
