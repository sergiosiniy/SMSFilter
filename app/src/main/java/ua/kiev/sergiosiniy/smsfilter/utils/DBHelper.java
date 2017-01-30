package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "filter_db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        dbCreatorUpdater(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dbCreatorUpdater(sqLiteDatabase,oldVersion, newVersion);
    }

    private void dbCreatorUpdater(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        if(oldVersion<newVersion) {
            // Contains the words to filter a message if it contains one or more of them
            sqLiteDatabase.execSQL("CREATE TABLE FILTERED_WORDS " +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT");
            /* Contains names of contacts and excepted numbers which was not added to contacts,
             which messages from are not a spam */
            sqLiteDatabase.execSQL("CREATE TABLE EXCEPTIONS " +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT");
            // All the phone numbers for filter exceptions
            sqLiteDatabase.execSQL("CREATE TABLE PHONES " +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, PHONE_NUMBER TEXT, NAME_ID INTEGER" +
                    "FOREIGN KEY(NAME_ID) REFERENCES EXCEPTIONS(_id)");
            /*Contains messages which contains the word matches to some from FILTER_WORDS table
            and number was never seen before. User can decide to add this number to
            exceptions(whitelist) or to phone contacts or do nothing to say it's spam.
            */
            sqLiteDatabase.execSQL("CREATE TABLE QUARANTINED " +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, PHONE_NUMBER TEXT, MESSAGE TEXT");
        }
    }
}
