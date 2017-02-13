package ua.kiev.sergiosiniy.smsfilter.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by SergioSiniy on 13.02.2017.
 */

public class ExceptionNamesTable {

    //FilteredWords table
    public static final String TABLE_NAME = "EXCEPTED_NAMES";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DATE = "DATE";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DATE + " TEXT" +
            ")";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ExceptionNamesTable.class.getName(), "Upgrading table from version " + oldVersion + " to " +
                newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
