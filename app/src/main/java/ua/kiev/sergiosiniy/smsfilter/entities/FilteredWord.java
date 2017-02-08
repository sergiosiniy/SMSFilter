package ua.kiev.sergiosiniy.smsfilter.entities;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilteredWord {

    public final static String TABLE_NAME = "FILTERED_WORDS";
    public final static String ROW_ID = "_ID";
    public final static String WORD = "WORD";

    private int _id;
    private String filteredWord;

    public FilteredWord(int id, String word) {
        this._id = id;
        this.filteredWord = word;
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


}
