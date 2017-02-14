package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.BroadcastReceiver;

/**
 * Created by Admin on 14.02.2017.
 */

public abstract class DatabaseChangedReceiver extends BroadcastReceiver {
    public static final String ACTION_ENTITY_INSERTED = "ua.kiev.sergiosiniy.ENTITY_INSERTED";
    public static final String ACTION_ENTITY_DELETED = "ua.kiev.sergiosiniy.ENTITY_DELETED";
}
