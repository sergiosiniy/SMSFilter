package ua.kiev.sergiosiniy.smsfilter.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;

import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;

/**
 * Created by SergioSiniy on 06.02.2017.
 */

public class Quarantined {

    public final static String TABLE_NAME = "QUARANTINED";
    public final static String ROW_ID = "_ID";
    public final static String PHONE_NUMBER = "PHONE_NUMBER";
    public final static String MESSAGE = "MESSAGE";

    private int _id;
    private String phoneNumber;
    private String messageBody;
    public static ArrayList<Quarantined> messages;

    public Quarantined(int id, String phone, String message){
        this._id=id;
        this.phoneNumber=phone;
        this.messageBody=message;
    }


    public int get_id() {
        return _id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

   /* public static ArrayList<Quarantined> getMessagesList(DBHelper helper){
        ArrayList<Quarantined> messages = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor mesCur = db.query(TABLE_NAME,null,null,null,null,null,null);


        while(mesCur.moveToNext()){
            messages.add(new Quarantined(mesCur.getInt(0),mesCur.getString(1),mesCur.getString(2)));
        }
        if(messages.size()==0){
            messages.add(new Quarantined(1,"","no items"));
        }
        mesCur.close();
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
