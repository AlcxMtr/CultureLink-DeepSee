package com.example.deepsee.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.content.ContentResolver;

import java.util.ArrayList;

public class Contact {

    private final String name;

    private final String contactNumber;

    public Contact (String name, String contactNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public String getName () {
        return name;
    }

    public String getContactNumber () {
        return contactNumber;
    }

    public static ArrayList<Contact> getContacts(Context context) {

        ArrayList <Contact> contacts = new ArrayList<>();
        String[] projection = new String[]{
                ContactsContract.Profile._ID,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
                ContactsContract.Profile.LOOKUP_KEY,
                ContactsContract.Profile.HAS_PHONE_NUMBER
        };
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int displayName = cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME);

                if (id >= 0 && displayName >= 0) {
                    String contactID = cursor.getString(id);
                    Cursor phoneNumCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactID}, null);

                    if (phoneNumCursor.moveToFirst()) {
                        int num = phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        if (num >= 0) {
                            contacts.add(new Contact(cursor.getString(displayName), phoneNumCursor.getString(num)));
                        }
                    }
                    phoneNumCursor.close();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return contacts;
    }

}
