package com.example.deepsee;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.deepsee.SMSMessages;

public class SMSReader {

    public List<SMSMessages> readSMS(Context context, ArrayList <String> names) {
        List<SMSMessages> smsMessages = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        int num = 0;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (num == 100) {
                    break;
                }
                int addressIndex = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                int bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY);
                int timeIndex = cursor.getColumnIndex(Telephony.Sms.DATE);
                

                String address = cursor.getString(addressIndex);
                address = getContactNameFromPhoneNumber(context, address);
                if (address == null) {
                    continue;
                }
                String body = cursor.getString(bodyIndex);
                long timeMillis = cursor.getLong(timeIndex);

                Date timestamp = null;
                String time = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault()).format(new Date(timeMillis));
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

                try {
                    timestamp = dateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Create a short version of the message body
                String shortBody = body.length() > 50 ? body.substring(0, 50) + "..." : body; // Adjust the length as needed

                // Check if the address already exists in the list
                boolean addressExists = false;
                for (SMSMessages message : smsMessages) {
                    if (message.getContactName().equals(address)) {
                         //Update the existing message if the address matches
                        if (message.getTimestamp().before(timestamp)) {
                            message.setMessage(shortBody);
                            message.setTimestamp(timestamp);
                        }
                        addressExists = true;
                        break;
                    }
                }

                // If the address doesn't exist, add a new message to the list
                if (!addressExists) {
                    SMSMessages smsMessage = new SMSMessages(address, shortBody, timestamp);
                    smsMessages.add(smsMessage);
                }
                num++;
            }
            cursor.close();
        }


        return smsMessages;
    }

    private static String getContactNameFromPhoneNumber(Context context, String phoneNumber) {
        String contactName = null;
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = contentResolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    if (index != -1) {
                        contactName = cursor.getString(index);
                    }
                }
            } finally {
                cursor.close();
            }
        }

        return contactName;
    }
}

