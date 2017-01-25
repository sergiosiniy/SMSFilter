package ua.kiev.sergiosiniy.smsfilter.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;

/**
 * Created by Admin on 25.01.2017.
 */

public class SMSFilterService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SMSFilterService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private class SMSReceiver extends BroadcastReceiver {

        private final String TAG = this.getClass().getSimpleName();
        private SQLiteDatabase db;
        SQLiteOpenHelper dbHelper;

        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle smsBundle = intent.getExtras();
            String stringMessageSource;
            String stringMessageBody;
            SmsMessage currentMessage;

            dbHelper = new DBHelper(SMSFilterService.this);
            db = dbHelper.getReadableDatabase();

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
                        new FilteredWordsCheck().execute(stringMessageSource, stringMessageBody);
                        Log.i(TAG, "Received message from" + stringMessageSource + ":" +
                                stringMessageBody);
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

                boolean isPassed = true;

                try {
                    Cursor filteredWordsCursor = db.query("FILTERED_WORDS",
                            new String[]{"WORD"},
                            null, null, null, null, null);
                    Cursor exceptedNumbersCursor = db.query("EXCEPTIONS",
                            new String[]{"NAME", "PHONE_NUMBER"},
                            null, null, null, null, null);
                    while (filteredWordsCursor.moveToNext()) {
                        if (message[1].toLowerCase().contains(" " +
                                filteredWordsCursor.getString(1).toLowerCase() + " ")) {
                            isPassed=false;
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
}
