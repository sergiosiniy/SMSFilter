package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by helpdeskss on 03.01.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "filter_db";
    private static final int DB_VERSION = 1;

    DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        dbCreatorUpdator(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void dbCreatorUpdator(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        if(oldVersion<1){
            sqLiteDatabase.execSQL("CREATE TABLE WORDS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"WORD TEXT, "
                    +"DESCRIPTION TEXT");
        }
    }
}
