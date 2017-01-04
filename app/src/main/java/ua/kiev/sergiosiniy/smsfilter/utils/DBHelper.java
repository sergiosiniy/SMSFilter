package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "filter_db";
    private static final int DB_VERSION = 1;

    DBHelper(Context context){
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
        if(oldVersion<newVersion){
            sqLiteDatabase.execSQL("CREATE TABLE WORDS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"WORD TEXT, ");
            sqLiteDatabase.execSQL("CREATE TABLE EXCEPTIONS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +", NAME TEXT, PHONE_NUMBER TEXT");
            sqLiteDatabase.execSQL("CREATE TABLE QUARANTINED (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"PHONE_NUMBER TEXT, MESSAGE TEXT");
        }
    }
}
