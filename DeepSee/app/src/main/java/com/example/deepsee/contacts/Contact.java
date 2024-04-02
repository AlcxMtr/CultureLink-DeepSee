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
                    System.out.println(cursor.getString(displayName));
                    Cursor phoneNumCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactID}, null);

                    if (phoneNumCursor.moveToFirst()) {
                        int num = phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        if (num >= 0) {
                            //String name = "ss";
                            System.out.print("," + cleanPhoneNumber(phoneNumCursor.getString(num)) );
                            contacts.add(new Contact(cursor.getString(displayName), cleanPhoneNumber(phoneNumCursor.getString(num))));
                        }
                    }
                    phoneNumCursor.close();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return contacts;
    }

    public static ArrayList<Contact> getContactsPhone(Context context) {
        ArrayList<Contact> contactsList = new ArrayList<>();

        // Query the contacts content provider
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                String contactId = cursor.getString(id);
                int name = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String contactName = cursor.getString(name);

                // Get the phone number(s) associated with the contact
                Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null);

                if (phoneCursor != null) {
                    while (phoneCursor.moveToNext()) {
                        int num = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNumber = phoneCursor.getString(num);
                        contactsList.add(new Contact(contactName, phoneNumber));
                    }
                    phoneCursor.close();
                }
            }
            cursor.close();
        }

        return contactsList;
    }



    private static String cleanPhoneNumber(String phoneNumber) {
        StringBuilder cleanedNumber = new StringBuilder();
        for (char c : phoneNumber.toCharArray()) {
            if (Character.isDigit(c)) {
                cleanedNumber.append(c);
            }
        }
        return cleanedNumber.toString();
    }


}
