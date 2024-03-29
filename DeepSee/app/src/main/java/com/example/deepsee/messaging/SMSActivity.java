package com.example.deepsee.messaging;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deepsee.R;
import com.example.deepsee.contacts.Contact;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class SMSActivity extends AppCompatActivity {

    private Handler handler;
    ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        contacts = Contact.getContacts(SMSActivity.this);

        for (Contact contact: contacts) {
            System.out.println("name: " + contact.getName() + " num: " + contact.getContactNumber());
        }
        SMSReader smsReader = new SMSReader();
        List<SMSMessages> smsMessages = smsReader.readSMS(SMSActivity.this, contacts,contacts.size());
        displaySMSMessages(smsMessages);

        handler = new Handler();
        handler.postDelayed(updateTask, 1000);
    }

    private Runnable updateTask = new Runnable() {
        @Override
        public void run() {

            SMSReader smsReader = new SMSReader();
            List<SMSMessages> smsMessages = smsReader.readSMS(SMSActivity.this, contacts,contacts.size());
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
                    intent.setData(Uri.parse("smsto:" + smsMessage.getContactName()));
                    // Opens default messaging app with the specific contact
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTask);
    }
}
