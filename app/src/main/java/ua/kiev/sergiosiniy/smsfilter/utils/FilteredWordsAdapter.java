package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.entities.FilteredWord;
import ua.kiev.sergiosiniy.smsfilter.tables.FilteredWordsTable;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilteredWordsAdapter extends RecyclerView.Adapter<FilteredWordsAdapter.ViewHolder> {

    private DatabaseChangedReceiver changesReceiver;
    private Cursor filteredWords;
    private Context mContext;
    private int listItemDeletePosition;

    public FilteredWordsAdapter(Context context, Cursor list) {
        this.filteredWords = list;
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DatabaseChangedReceiver.ACTION_ENTITY_INSERTED);
        intentFilter.addAction(DatabaseChangedReceiver.ACTION_ENTITY_DELETED);
        changesReceiver = new DatabaseChangedReceiver() {
            @Override
            public void onReceive(Context receiveContext, Intent intent) {
               new UpdateCursor().execute(intent);
            }
        };
        context.registerReceiver(changesReceiver, intentFilter);
    }

    @Override
    public FilteredWordsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View wordsView = inflater.inflate(R.layout.filtered_list_item_view, parent, false);

        return new ViewHolder(wordsView);
    }

    @Override
    public void onBindViewHolder(final FilteredWordsAdapter.ViewHolder holder, final int position) {
        filteredWords.moveToPosition(position);
        FilteredWord filtered = new FilteredWord(filteredWords.getInt(0),
                filteredWords.getString(1));

        final TextView word = holder.word;
        word.setText(filtered.getFilteredWord());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemDeletePosition = holder.getAdapterPosition();
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                final View inflateView = layoutInflater.inflate(R.layout.dialog_delete_word_view,
                        null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext)
                        .setTitle(R.string.dialog_delete_word_from_filter)
                        .setView(inflateView);

                TextView item = (TextView) holder.itemView.findViewById(R.id.filtered_word);

                final String wordToDelete = item.getText().toString();


                TextView dialogTextView = (TextView) inflateView
                        .findViewById(R.id.textview_delete_word_dialog);
                dialogTextView.setText(wordToDelete);

                dialogBuilder.setPositiveButton(R.string.positive_button_dialog_delete_word,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new FilteredWordsAdapter.UpdateFilteredWords().execute(
                                        wordToDelete);

                            }
                        })
                        .setNegativeButton(R.string.negative_button_dialog_delete_word,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                dialogBuilder.create();
                dialogBuilder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredWords.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView word;

        public ViewHolder(View itemView) {
            super(itemView);
            word = (TextView) itemView.findViewById(R.id.filtered_word);

        }
    }

    private Context getContext() {
        return mContext;
    }

    private class UpdateFilteredWords extends AsyncTask<String, Void, Void> {

        SQLiteOpenHelper helper = new DBHelper(mContext);
        SQLiteDatabase db;

        @Override
        protected Void doInBackground(String... params) {

            db = helper.getWritableDatabase();
            try {

                db.delete(FilteredWordsTable.TABLE_NAME, FilteredWordsTable.COLUMN_WORD +
                        "=\'" + params[0] + "\'", null);
                db.close();
            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.e("UpdateFilteredWords:", "Can't get access to the DB!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            mContext.sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_ENTITY_DELETED));
        }
    }

    private class UpdateCursor extends AsyncTask<Intent,Void,Intent>{
        SQLiteOpenHelper helper = new DBHelper(mContext);
        SQLiteDatabase db;

        @Override
        protected Intent doInBackground(Intent... params) {

            db=helper.getReadableDatabase();
            filteredWords.close();
            filteredWords=db.query(FilteredWordsTable.TABLE_NAME,null,null,null,null,null,null);
            return params[0];
        }

        @Override
        protected void onPostExecute(Intent intent) {
            switch (intent.getAction()){
                case DatabaseChangedReceiver.ACTION_ENTITY_INSERTED:
                    notifyItemInserted(getItemCount());
                    notifyItemRangeChanged(getItemCount(),getItemCount());
                    break;
                case DatabaseChangedReceiver.ACTION_ENTITY_DELETED:
                    notifyItemRemoved(listItemDeletePosition);
                    notifyItemRangeChanged(listItemDeletePosition,getItemCount());
                    break;
            }
            db.close();
        }
    }

}
