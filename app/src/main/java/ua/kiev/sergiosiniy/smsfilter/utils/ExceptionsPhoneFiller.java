package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Created by SergioSiniy on 27.01.2017.
 */

public class ExceptionsPhoneFiller {

    String phoneNumber = null;
    String id = null;
    String name = null;

    public void fillTheExceptionsTable(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactPhones = contentResolver.query(ContactsContract
                .Contacts.CONTENT_URI, null, null, null, null);

        if (contactPhones!= null) {

            while (contactPhones.moveToNext()) {
                id = contactPhones.getString(contactPhones.getColumnIndex(ContactsContract.Contacts._ID));

                name = contactPhones.getString(contactPhones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(contactPhones.getString(contactPhones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phonesCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (phonesCur.moveToNext()) {
                        phoneNumber = phonesCur.getString(phonesCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                    }
                    phonesCur.close();
                }
            }

        }
        contactPhones.close();
    }
}
