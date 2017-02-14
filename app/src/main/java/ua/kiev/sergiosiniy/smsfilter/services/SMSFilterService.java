package ua.kiev.sergiosiniy.smsfilter.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import ua.kiev.sergiosiniy.smsfilter.tables.ExceptionsTable;
import ua.kiev.sergiosiniy.smsfilter.tables.FilteredWordsTable;
import ua.kiev.sergiosiniy.smsfilter.tables.QuarantinedTable;
import ua.kiev.sergiosiniy.smsfilter.utils.ContactPhonesCheck;
import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;

/**
 * Created by SergioSiniy on 25.01.2017.
 */

public class SMSFilterService extends IntentService {
    public static boolean SERVICE_STATUS = false;

    private SMSReceiver messageReceiver;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public SMSFilterService() {
        super("SMSFilterService");
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //Creates object of SMSReceiver class
        messageReceiver = new SMSReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        //Registering SMS Receiver
        registerReceiver(messageReceiver, mIntentFilter);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context = getApplicationContext();
        } else {
            context = getApplication();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    @Override
    public void onDestroy() {
        unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    /**
     * SMSReceiver class, which waiting for SMS receiving and provides
     * check if the message contains filtered words. If it contains a filtered word - the
     * receiver will check if the originating number stored in the phones table. If it is
     * the message passes to the SMS App, else message will be quarantined and user will see
     * the notification.
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
                Log.e(TAG, "NPE was caught in onReceive() method SMSReceiver class!");
                e.printStackTrace();
            }
        }

        /**
         * Provides check for filtered words and excepted phones. Decides weather SMS
         * should be delivered or pushed to the quarantine.
         */
        private class FilteredWordsCheck extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... message) {

                // This value will allow the message to pass to the SMS Inbox
                boolean isPassed = true;

                try {
                    // Get all the filtered words from DB
                    Cursor filteredWordsCursor = db.query(FilteredWordsTable.TABLE_NAME,
                            new String[]{FilteredWordsTable.COLUMN_WORD},
                            null, null, null, null, null);

                    // Get all the phones from DB
                    Cursor exceptedNumbersCursor = db.query(ExceptionsTable.TABLE_NAME,
                            new String[]{ExceptionsTable.COLUMN_PHONES},
                            null, null, null, null, null);

                    if (filteredWordsCursor != null && filteredWordsCursor.getCount() > 0) {
                        while (filteredWordsCursor.moveToNext()) {
                            if (message[1].toLowerCase().contains(filteredWordsCursor.getString(0)
                                    .toLowerCase())) {
                                isPassed = false;
                                Log.i(TAG, "Message from" + message[0] + ":" + message[1] +
                                        "CONTAINS FILTERED WORD!");

                                // Checks if the phone book contains phone number from a message
                                ContactPhonesCheck contactPhonesCheck = new ContactPhonesCheck();
                                isPassed = contactPhonesCheck.checkContacts(context, message[0]);

                                if (exceptedNumbersCursor != null && exceptedNumbersCursor
                                        .getCount() > 0 && !isPassed) {
                                    while (exceptedNumbersCursor.moveToNext()) {
                                        if (exceptedNumbersCursor.getString(0).equals(message[0])) {
                                            isPassed = true;
                                            Log.i(TAG, "Message from" + message[0] + ":" +
                                                    message[1] + "CONTAINS EXCEPTED PHONE!");
                                            break;
                                        }
                                    }
                                    exceptedNumbersCursor.close();
                                }
                            }
                        }
                        filteredWordsCursor.close();
                    } else {

                        exceptedNumbersCursor.close();
                        Log.v(TAG, "No match word found.");
                    }

                    db.close();

                    // If message not passed - put it to the quarantined table
                    if (!isPassed) {
                        ContentValues quarantined = new ContentValues();
                        quarantined.put(QuarantinedTable.COLUMN_PHONE, message[0]);
                        quarantined.put(QuarantinedTable.COLUMN_MESSAGE, message[1]);
                        db = dbHelper.getWritableDatabase();
                        db.insert(QuarantinedTable.TABLE_NAME,
                                null, quarantined);
                    }

                    db.close();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Can't get access to the DB");
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
}
