package ua.kiev.sergiosiniy.smsfilter.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;

import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilterException {

    public final static String TABLE_NAME = "EXCEPTED_PHONES";
    public final static String ROW_ID = "_ID";
    public final static String EXCEPTION_PHONE = "PHONE_NUMBER";
    public final static String NAME_ID = "NAME_ID";

    private int _id;
    private String exceptedPhone;
    private int nameId;



    public FilterException(int id, String word, int name){
        this._id=id;
        this.exceptedPhone =word;
        this.nameId=name;
    }


    public int get_id() {
        return _id;
    }

    public String getPhoneNumber() {
        return exceptedPhone;
    }

    public int getNameId() {
        return nameId;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.exceptedPhone = phoneNumber;
    }

    public void setNameId(int name) {
        this.nameId = name;
    }


    public static ArrayList<FilterException> getFilteredList(DBHelper helper){
        ArrayList<FilterException> messages = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor exceptCur = db.query(TABLE_NAME,null,null,null,null,null,null);


        while(exceptCur.moveToNext()){
            messages.add(new FilterException(exceptCur.getInt(0),exceptCur.getString(1),
                    exceptCur.getInt(2)));
        }
        if(messages.size()==0){
            messages.add(new FilterException(1,"no items",1));
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
