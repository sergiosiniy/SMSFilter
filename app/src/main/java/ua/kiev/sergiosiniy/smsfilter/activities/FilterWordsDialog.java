package ua.kiev.sergiosiniy.smsfilter.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.entities.FilteredWord;
import ua.kiev.sergiosiniy.smsfilter.tables.FilteredWordsTable;

/**
 * Created by SergioSiniy on 09.02.2017.
 */

public class FilterWordsDialog {

    private final String INSERT="insert";
    private final String DELETE="delete";
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private SQLiteOpenHelper helper;


    public FilterWordsDialog(Context context, AlertDialog.Builder builder,
                             SQLiteOpenHelper helper){
        this.context = context;
        this.dialogBuilder=builder;
        this.helper=helper;
    }

    public AlertDialog.Builder setAddWordToFilter(){


        return this.dialogBuilder;
    }

    public AlertDialog.Builder setDeleteWordToFilter(final RecyclerView recyclerView, final int param){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View inflateView = layoutInflater.inflate(R.layout.dialog_delete_word_view, null);
        dialogBuilder = new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_add_word_to_filter)
                .setView(inflateView);

        dialogBuilder.setPositiveButton(R.string.positive_button_dialog_add_word,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(param!=-1) {
                    TextView item = (TextView) recyclerView.getChildAt(param);
                    new FilterWordsDialog.UpdateFilteredWords().execute(DELETE,item.getText()
                            .toString());
                }


                Toast.makeText(context, R.string.toast_empty_string_dialog_add_word,
                        Toast.LENGTH_LONG).show();

            }
        })
                .setNegativeButton(R.string.negative_button_dialog_add_word,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return this.dialogBuilder;
    }

    private class UpdateFilteredWords extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            SQLiteDatabase db=helper.getWritableDatabase();
            if(params[0].equals(INSERT)) {
                ContentValues wordToFilter = new ContentValues();
                wordToFilter.put(FilteredWordsTable.COLUMN_WORD, params[1]);

                db.insert(FilteredWordsTable.TABLE_NAME, null, wordToFilter);
            }
            if(params[0].equals(DELETE)){
                db.delete(FilteredWordsTable.TABLE_NAME,FilteredWordsTable.COLUMN_WORD,
                        new String[]{params[1]});
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

    }


