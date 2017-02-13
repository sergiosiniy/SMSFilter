package ua.kiev.sergiosiniy.smsfilter.entities;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilterExceptionName {

    private int _id;
    private String exceptedPhoneName;
    private String date;

    public FilterExceptionName(int id, String name, String date) {
        this._id = id;
        this.exceptedPhoneName = name;
        this.date = date;
    }

    public int get_id() {
        return _id;
    }

    public String getExceptedPhoneName() {
        return exceptedPhoneName;
    }

    public String getDate() {
        return this.date;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setExceptedPhoneName(String phoneName) {
        this.exceptedPhoneName = phoneName;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
