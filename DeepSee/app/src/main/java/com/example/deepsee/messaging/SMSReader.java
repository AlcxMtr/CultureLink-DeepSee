package com.example.deepsee.messaging;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;

import com.example.deepsee.contacts.Contact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SMSReader {

    public List<SMSMessages> readSMS(Context context, ArrayList <Contact> contacts, int numofcontacts) {
        List<SMSMessages> smsMessages = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = Uri.parse("content://sms/");

        String [] projections = {
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE
        };

        Cursor cursor = contentResolver.query(uri, projections, null, null, null);
        int num = 0;


        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (num == numofcontacts) {
                    break;
                }
                int addressIndex = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                int bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY);
                int timeIndex = cursor.getColumnIndex(Telephony.Sms.DATE);
                

                String address = cursor.getString(addressIndex);

                boolean exists = false;

                for (Contact contact: contacts) {
                    if (contact.getName().equals(address) || contact.getContactNumber().equals(address)) {
                        exists = true;
                        address = contact.getName();
                        break;
                    }
                }

                if (!exists) {
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
                    num++;
                }

            } while (cursor.moveToNext());
            cursor.close();
        }


        return smsMessages;
    }

}

