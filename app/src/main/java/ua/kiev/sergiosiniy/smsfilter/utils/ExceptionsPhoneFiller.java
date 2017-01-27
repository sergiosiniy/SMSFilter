package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Created by SergioSiniy on 27.01.2017.
 */

public class ExceptionsPhoneFiller {

    public void fillTheExceptionsTable(Context context) {
        String name = null;
        String phoneNumber = null;
        ContentResolver contentResolver = context.getContentResolver();

        //get all contacts from phone book
        Cursor contactsCursor = contentResolver.query(ContactsContract
                .Contacts.CONTENT_URI, null, null, null, null);

        if (contactsCursor!= null) {

            while (contactsCursor.moveToNext()) {

                name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract
                        .Contacts.DISPLAY_NAME));

                if (Integer.parseInt(contactsCursor.getString(contactsCursor
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phonesCursor = contentResolver.query(ContactsContract.CommonDataKinds
                            .Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?",
                            new String[]{name}, null);

                    while (phonesCursor.moveToNext()) {
                        phoneNumber = phonesCursor.getString(phonesCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds
                                        .Phone.NORMALIZED_NUMBER));
                    }

                    phonesCursor.close();
                }
            }

            contactsCursor.close();
        }
    }
}
