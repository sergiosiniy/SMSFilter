package ua.kiev.sergiosiniy.smsfilter.entities;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilterException {

    private int _id;
    private String exceptedPhone;
    private int nameId;


    public FilterException(int id, String word, int name) {
        this._id = id;
        this.exceptedPhone = word;
        this.nameId = name;
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

}
