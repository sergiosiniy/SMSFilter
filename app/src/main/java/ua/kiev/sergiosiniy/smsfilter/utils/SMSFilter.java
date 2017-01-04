package ua.kiev.sergiosiniy.smsfilter.utils;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.R.id.message;

public class SMSFilter extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle smsBundle = intent.getExtras();
        String receivedPhoneNumber;
        String message;
        SmsMessage currentMessage;

        try {
            if (smsBundle != null) {
                final Object[] pduObjects = (Object[]) smsBundle.get("pdus");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    for (Object pdu : pduObjects) {
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdu,
                                "3gpp");
                        receivedPhoneNumber = currentMessage.getDisplayOriginatingAddress();
                        message = currentMessage.getDisplayMessageBody();
                        //TODO
                        //check if there filtered words in message
                        //if message contains filtered words get phone numbers from contacts and compare with @receivedPhoneNumber
                        //get phone numbers from excepted list and compare with @receivedPhoneNumber
                        //if no
                    }
                } else {
                    for (Object pdu : pduObjects) {
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        receivedPhoneNumber = currentMessage.getDisplayOriginatingAddress();
                        message = currentMessage.getDisplayMessageBody();
                    }
                }
            }
        }catch(NullPointerException e){
            Log.e("Exception!", "NPE was caught in onReceive() method util.SMSFilter class!");
            e.printStackTrace();
        }
    }
}
