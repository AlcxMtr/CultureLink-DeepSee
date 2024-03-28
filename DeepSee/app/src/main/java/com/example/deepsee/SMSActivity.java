package com.example.deepsee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SMSActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        SMSReader smsReader = new SMSReader();
        List<SMSMessages> smsMessages = smsReader.readSMS(SMSActivity.this);
        displaySMSMessages(smsMessages);
    }

    private void displaySMSMessages(List<SMSMessages> smsMessages) {
        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        for (SMSMessages smsMessage : smsMessages) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.layout_msgs, null);
            TextView contactNameTextView = cardView.findViewById(R.id.contact_name);
            TextView timeTextView = cardView.findViewById(R.id.timeofmsg);
            TextView messageTextView = cardView.findViewById(R.id.textmsg_shortened);

            contactNameTextView.setText(smsMessage.getContactName());
            timeTextView.setText(smsMessage.getTimestamp());
            messageTextView.setText(smsMessage.getMessage());

            linearLayout.addView(cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                    startActivity(intent);
                }
            });
        }
    }
}
