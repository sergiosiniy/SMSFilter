package ua.kiev.sergiosiniy.smsfilter.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by SergioSiniy on 13.02.2017.
 */

public class ExceptionsTable {

    //FilteredWords table
    public static final String TABLE_NAME = "EXCEPTED_PHONES";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_PHONES = "PHONE_NUMBER";
    public static final String COLUMN_NAME_ID = "NAME_ID";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PHONES + " TEXT, " +
            COLUMN_NAME_ID + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_NAME_ID + ") REFERENCES " +
            ExceptionNamesTable.TABLE_NAME + "(" + ExceptionNamesTable.COLUMN_ID + ")" +
            ")";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ExceptionsTable.class.getName(), "Upgrading table from version " + oldVersion +
                " to " + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
