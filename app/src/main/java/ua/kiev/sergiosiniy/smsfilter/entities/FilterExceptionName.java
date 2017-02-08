package ua.kiev.sergiosiniy.smsfilter.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;

import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilterExceptionName {

    public final static String TABLE_NAME = "EXCEPTED_NAMES";
    public final static String ROW_ID = "_ID";
    public final static String EXCEPTION_NAME = "NAME";
    public final static String EXCEPTION_NAME_ADD_DATE = "DATE";


    private int _id;
    private String exceptedPhoneName;
    private String date;




    public FilterExceptionName(int id, String name, String date){
        this._id=id;
        this.exceptedPhoneName = name;
        this.date=date;
    }


    public int get_id() {
        return _id;
    }

    public String getExceptedPhoneName() {
        return exceptedPhoneName;
    }

    public String getDate(){
        return this.date;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setExceptedPhoneName(String phoneName) {
        this.exceptedPhoneName = phoneName;
    }

    public void setDate(String date){
        this.date=date;
    }


    public static ArrayList<FilterExceptionName> getExceptionNamesList(DBHelper helper){
        ArrayList<FilterExceptionName> messages = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor exceptCur = db.query(TABLE_NAME,null,null,null,null,null,null);


        while(exceptCur.moveToNext()){
            messages.add(new FilterExceptionName(exceptCur.getInt(0),exceptCur.getString(1),
                    exceptCur.getString(2)));
        }
        if(messages.size()==0){
            messages.add(new FilterExceptionName(1,"no name",""));
        }
        exceptCur.close();
        db.close();
        return messages;
    }

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

    private class GetEntities extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
