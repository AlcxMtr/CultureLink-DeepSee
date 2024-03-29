package com.example.deepsee;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SMSActivity extends AppCompatActivity {

    private Handler handler;
    ArrayList<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        contacts = getContacts();
        SMSReader smsReader = new SMSReader();
        List<SMSMessages> smsMessages = smsReader.readSMS(SMSActivity.this, contacts);
        displaySMSMessages(smsMessages);

        handler = new Handler();
        handler.postDelayed(updateTask, 1000);
    }

    private Runnable updateTask = new Runnable() {
        @Override
        public void run() {

            SMSReader smsReader = new SMSReader();
            List<SMSMessages> smsMessages = smsReader.readSMS(SMSActivity.this, contacts);
            displaySMSMessages(smsMessages);
            handler.postDelayed(this, 1000);
        }
    };

    private void displaySMSMessages(List<SMSMessages> smsMessages) {
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        linearLayout.removeAllViews();

        for (SMSMessages smsMessage : smsMessages) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.layout_msgs, null);
            TextView contactNameTextView = cardView.findViewById(R.id.contact_name);
            TextView timeTextView = cardView.findViewById(R.id.timeofmsg);
            TextView messageTextView = cardView.findViewById(R.id.textmsg_shortened);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String time = dateFormat.format(smsMessage.getTimestamp());

            contactNameTextView.setText(smsMessage.getContactName());
            timeTextView.setText(time);
            messageTextView.setText(smsMessage.getMessage());

            linearLayout.addView(cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("smsto:" + smsMessage.getContactName())); // Opens default messaging app with the specific contact
                    startActivity(intent);
                }
            });
        }
    }

    private ArrayList<String> getContacts() {

        ArrayList <String> names = new ArrayList<>();
        String[] projection = new String[]{
                ContactsContract.Profile._ID,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
                ContactsContract.Profile.LOOKUP_KEY,
                ContactsContract.Profile.HAS_PHONE_NUMBER
        };
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int displayName = cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME);

                if (id >= 0 && displayName >= 0) {
                    String contactID = cursor.getString(id);
                    Cursor phoneNumCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactID}, null);

                    if (phoneNumCursor.moveToFirst()) {
                        int num = phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        if (num >= 0) {
                            names.add(cursor.getString(displayName));
                        }
                    }
                    phoneNumCursor.close();


                }


            } while (cursor.moveToNext());
        }
        cursor.close();

        return names;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTask);
    }
}
