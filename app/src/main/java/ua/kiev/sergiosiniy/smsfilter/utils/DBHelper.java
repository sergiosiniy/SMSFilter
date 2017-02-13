package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.kiev.sergiosiniy.smsfilter.tables.ExceptionNamesTable;
import ua.kiev.sergiosiniy.smsfilter.tables.ExceptionsTable;
import ua.kiev.sergiosiniy.smsfilter.tables.FilteredWordsTable;
import ua.kiev.sergiosiniy.smsfilter.tables.QuarantinedTable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "filter_db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Contains the words to filter a message if it contains one or more of them
        FilteredWordsTable.onCreate(sqLiteDatabase);

            /* Contains names of contacts and excepted numbers which was not added to contacts,
             which messages from are not a spam */
        ExceptionNamesTable.onCreate(sqLiteDatabase);

        // A phone numbers for filter exceptions, not from contacts
        ExceptionsTable.onCreate(sqLiteDatabase);

            /*Contains messages which contains the word matches to some from FILTER_WORDS table
            and number was never seen before. User can decide to add this number to
            exceptions(whitelist) or to phone contacts or do nothing to say it's spam.
            */
        QuarantinedTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        FilteredWordsTable.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        ExceptionNamesTable.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        ExceptionsTable.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        QuarantinedTable.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
    }
}
