package ua.kiev.sergiosiniy.smsfilter.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;

import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilteredWord {

    public final static String TABLE_NAME = "FILTERED_WORDS";
    public final static String ROW_ID = "_ID";
    public final static String WORD = "WORD";

    private int _id;
    private String filteredWord;

    public FilteredWord(int id, String word){
        this._id=id;
        this.filteredWord=word;
    }


    public int get_id() {
        return _id;
    }

    public String getPhoneNumber() {
        return filteredWord;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.filteredWord = phoneNumber;
    }


   /* public static ArrayList<FilteredWord> getFilteredList(DBHelper helper){
        ArrayList<FilteredWord> messages = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor wordsCur = db.query(TABLE_NAME,null,null,null,null,null,null);

        while(wordsCur.moveToNext()){
            messages.add(new FilteredWord(wordsCur.getInt(0),wordsCur.getString(1)));
        }
        if(messages.size()==0){
            messages.add(new FilteredWord(1,"no items"));
        }
        wordsCur.close();
        db.close();
        return messages;
    }*/

   /* @Nullable
    public static Quarantined getQuarantined(int id,DBHelper helper){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor quarantineCur = db.query(TABLE_NAME,null,ROW_ID+"=?",
                new String[]{Integer.toString(id)},null,null,null);
        if(quarantineCur.moveToFirst()){
            return new Quarantined(quarantineCur.getInt(0),quarantineCur.getString(1),
                    quarantineCur.getString(2));
        }
        quarantineCur.close();
        db.close();
        return null;
    }*/

}
