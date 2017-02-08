package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Created by SergioSiniy on 27.01.2017.
 */

public class ContactPhonesCheck {

    public boolean checkContacts(Context context, String originsPhoneNumber) {
        String name;
        String phoneNumber;
        boolean passed = false;
        ContentResolver contentResolver = context.getContentResolver();

        //get all contacts from phone book
        Cursor contactsCursor = contentResolver.query(ContactsContract
                .Contacts.CONTENT_URI, null, null, null, null);

        if (contactsCursor != null) {
            externalLoop:
            while (contactsCursor.moveToNext()) {

                //get contact name
                name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract
                        .Contacts.DISPLAY_NAME));


                if (Integer.parseInt(contactsCursor.getString(contactsCursor
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phonesCursor = contentResolver.query(ContactsContract.CommonDataKinds
                                    .Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?",
                            new String[]{name}, null);

                    if (phonesCursor != null) {
                        while (phonesCursor.moveToNext()) {
                            phoneNumber = phonesCursor.getString(phonesCursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds
                                            .Phone.NORMALIZED_NUMBER));
                            //if phone book contains such number break loop and return true
                            if (phoneNumber.equals(originsPhoneNumber)) {
                                passed = true;
                                break externalLoop;
                            }
                        }

                        phonesCursor.close();
                    }
                }
            }

            contactsCursor.close();
        }
        return passed;
    }
}
