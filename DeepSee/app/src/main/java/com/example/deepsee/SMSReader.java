package com.example.deepsee;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.deepsee.SMSMessages;

public class SMSReader {

    public List<SMSMessages> readSMS(Context context) {
        List<SMSMessages> smsMessages = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int addressIndex = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                int bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY);
                int timeIndex = cursor.getColumnIndex(Telephony.Sms.DATE);

                String address = cursor.getString(addressIndex);
                String body = cursor.getString(bodyIndex);
                long timeMillis = cursor.getLong(timeIndex);

                String timestamp = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(timeMillis));

                // Create a short version of the message body
                String shortBody = body.length() > 50 ? body.substring(0, 50) + "..." : body; // Adjust the length as needed

                // Check if the address already exists in the list
                boolean addressExists = false;
                for (SMSMessages message : smsMessages) {
                    if (message.getContactName().equals(address)) {
                        // Update the existing message if the address matches
                        message.setMessage(shortBody);
                        message.setTimestamp(timestamp);
                        addressExists = true;
                        break;
                    }
                }

                // If the address doesn't exist, add a new message to the list
                if (!addressExists) {
                    SMSMessages smsMessage = new SMSMessages(address, shortBody, timestamp);
                    smsMessages.add(smsMessage);
                }
            }
            cursor.close();
        }


        return smsMessages;
    }
}

