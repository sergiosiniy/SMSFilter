package ua.kiev.sergiosiniy.smsfilter.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;
import ua.kiev.sergiosiniy.smsfilter.utils.ExceptionsPhoneFiller;

/**
 * Created by SergioSiniy on 25.01.2017.
 */

public class SMSFilterService extends IntentService {

    private SMSReceiver messageReceiver;
    private IntentFilter mIntentFilter;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private ContactsObserver contactsObserver;
    private ContentResolver contentResolver;

    public SMSFilterService() {
        super("SMSFilterService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*
            Creates object of contacts observer, which keeps a look and adds/deletes phones in the
            exceptions and phones tables when user changes his contact's info
         */
        contactsObserver = new ContactsObserver();
        contentResolver = getContentResolver();
        /*
            Creates object of SMSReceiver class
         */
        messageReceiver = new SMSReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        // Registering contacts observer
        this.getApplicationContext().getContentResolver()
                .registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                        true, contactsObserver);
        //Registering SMS Receiver
        registerReceiver(messageReceiver, mIntentFilter);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    @Override
    public void onDestroy() {
        this.getApplicationContext().getContentResolver().unregisterContentObserver(contactsObserver);
        unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    /*
       SMSReceiver class, which waiting for SMS receiving and provides
       check if the message contains filtered words. If it contains a filtered word - the
       receiver will check if the originating number stored in the phones table. If it is
       the message passes to the SMS App, else message will be quarantined and user will see
       the notification.
    */
    private class SMSReceiver extends BroadcastReceiver {

        // Tag with a current class name for android logging
        private final String TAG = this.getClass().getSimpleName();

        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle smsBundle = intent.getExtras();
            String stringMessageSource;
            String stringMessageBody;
            SmsMessage currentMessage;

            dbHelper = new DBHelper(SMSFilterService.this);
            db = dbHelper.getReadableDatabase();

            // Here I'm trying to get the message's body and originating address.
            try {
                if (smsBundle != null) {
                    final Object[] pduObjects = (Object[]) smsBundle.get("pdus");

                    for (Object pdu : pduObjects) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            currentMessage = SmsMessage.createFromPdu((byte[]) pdu, "3gpp");
                        } else {
                            currentMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        }

                        stringMessageSource = currentMessage.getDisplayOriginatingAddress();
                        stringMessageBody = currentMessage.getDisplayMessageBody();
                        // When I'm done I push it's phone number and text to the log.
                        Log.i(TAG, "Received message from" + stringMessageSource + ":" +
                                stringMessageBody);
                        // And begin the process of checking of the message for filtered words.
                        new FilteredWordsCheck().execute(stringMessageSource, stringMessageBody);

                    }
                }
            } catch (NullPointerException e) {
                Log.e("Exception!", "NPE was caught in onReceive() method SMSReceiver class!");
                e.printStackTrace();
            }
            dbHelper.close();
            db.close();
        }

        private class FilteredWordsCheck extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... message) {
                // This value will allow the message to pass to the SMS Inbox
                boolean isPassed = true;

                try {
                    // Get all the filtered words from DB
                    Cursor filteredWordsCursor = db.query("FILTERED_WORDS",
                            new String[]{"WORD"},
                            null, null, null, null, null);
                    // Get all the phones from DB
                    Cursor exceptedNumbersCursor = db.query("PHONES",
                            new String[]{"PHONE_NUMBER"},
                            null, null, null, null, null);
                    // Check if there any number in DB. If not - fill DB up!
                    if (exceptedNumbersCursor.getCount() == 0) {
                        /* This thing should fill exceptions and phone tables in my SQLite
                           from user's phone contacts. But I don't know if it does =(
                        */
                        ExceptionsPhoneFiller phoneFiller = new ExceptionsPhoneFiller();
                        phoneFiller.fillTheExceptionsTable(getApplicationContext());
                        // Try to get any phones from DB. Again.
                        exceptedNumbersCursor = db.query("PHONES",
                                new String[]{"PHONE_NUMBER"},
                                null, null, null, null, null);
                    }


                    while (filteredWordsCursor.moveToNext()) {
                        if (message[1].toLowerCase().contains(" " +
                                filteredWordsCursor.getString(0).toLowerCase() + " ")) {
                            isPassed = false;
                            Log.i(TAG, "Message from" + message[0] + ":" + message[1] +
                                    "CONTAINS FILTERED WORD!");
                            while (exceptedNumbersCursor.moveToNext()) {
                                if (message[0].contains(exceptedNumbersCursor.getString(0))) {
                                    isPassed = true;
                                    Log.i(TAG, "Message from" + message[0] + ":" + message[1] +
                                            "CONTAINS EXCEPTED PHONE!");
                                    break;
                                }
                            }
                            break;
                        } else {
                            Log.v(TAG, "No match with word " + filteredWordsCursor.getString(0)
                                    .toUpperCase() + " found.");
                        }
                    }

                    if (!isPassed) {
                        ContentValues quarantined = new ContentValues();
                        quarantined.put("PHONE_NUMBER", message[0]);
                        quarantined.put("MESSAGE", message[1]);
                        db = dbHelper.getWritableDatabase();
                        db.insert("QUARANTINED",
                                null, quarantined);
                    }

                    filteredWordsCursor.close();
                    exceptedNumbersCursor.close();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                    Log.e("SQLiteException", "No access to the DB");
                }

                return isPassed;
            }

            @Override
            protected void onPostExecute(Boolean isPassed) {
                if (!isPassed) {
                    SMSReceiver.this.abortBroadcast();
                }
            }
        }
    }

    /*
        ContactsObserver class, which keeps a look and adds/deletes phones in the
        exceptions and phones tables when user changes his contact's info.
     */
    private class ContactsObserver extends ContentObserver {

        ContactsObserver() {
            super(null);
        }


        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            ContentResolver contentResolver = getContentResolver();
            Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            ContentValues newContact = new ContentValues();

            //TODO on change/add of a contact in the phone book update/add this contact to the exceptions table
        }
    }
     /*TODO create private class which gets all contacts from the phone book ant puts it to
              TODO the exceptions table when app runs at the first time*/
}
