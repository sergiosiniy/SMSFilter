package ua.kiev.sergiosiniy.smsfilter.entities;

import java.util.ArrayList;

/**
 * Created by SergioSiniy on 06.02.2017.
 */

public class Quarantined {

    private int _id;
    private String phoneNumber;
    private String messageBody;
    public static ArrayList<Quarantined> messages;

    public Quarantined(int id, String phone, String message) {
        this._id = id;
        this.phoneNumber = phone;
        this.messageBody = message;
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


}
