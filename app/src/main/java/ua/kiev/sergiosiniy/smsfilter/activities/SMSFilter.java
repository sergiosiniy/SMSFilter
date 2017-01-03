package ua.kiev.sergiosiniy.smsfilter.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsMessage;

public class SMSFilter extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        String senderPhoneNumber;
        String message;
        String checkWord="taxi";

        String messageString="plain/text";
        if(intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction())==0){
            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");

            SmsMessage[] messages = new SmsMessage[pduArray.length];
            for(int i=0;i<pduArray.length;i++){
                if(Build.VERSION.SDK_INT>=23) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i], messageString);
                }else{
                    messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                }
                message=messages[i].getDisplayMessageBody();

                senderPhoneNumber = messages[i].getDisplayOriginatingAddress();
            }
        }
    }
}
