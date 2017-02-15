package ua.kiev.sergiosiniy.smsfilter.activities;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.tables.ExceptionNamesTable;
import ua.kiev.sergiosiniy.smsfilter.tables.FilteredWordsTable;
import ua.kiev.sergiosiniy.smsfilter.tables.QuarantinedTable;
import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;
import ua.kiev.sergiosiniy.smsfilter.utils.DatabaseChangedReceiver;
import ua.kiev.sergiosiniy.smsfilter.utils.ExceptionsAdapter;
import ua.kiev.sergiosiniy.smsfilter.utils.FilteredWordsAdapter;
import ua.kiev.sergiosiniy.smsfilter.utils.QuarantinedAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {

    private Context context;
    public static final String ITEM_ID = "item_id";
    private RecyclerView recyclerView;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private Cursor items;


    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context = getActivity();
        } else {
            context = getContext();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        helper = new DBHelper(context);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        recyclerView = (RecyclerView) view.findViewById(R.id.item_click_support);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (getArguments().getInt(ITEM_ID) != 0) {
            FloatingActionButton floatingActionButton =
                    (FloatingActionButton) view.findViewById(R.id.fab_recycler_view);

            switch (getArguments().getInt(ITEM_ID)) {
                case R.id.navigation_filtered:
                    new GetItemsCursor().execute(getArguments().getInt(ITEM_ID));
                    setFab(getArguments().getInt(ITEM_ID), floatingActionButton);
                    break;

                case R.id.navigation_quarantined:
                    new GetItemsCursor().execute(getArguments().getInt(ITEM_ID));
                    setFab(getArguments().getInt(ITEM_ID), floatingActionButton);
                    break;

                case R.id.navigation_exceptions:
                    new GetItemsCursor().execute(getArguments().getInt(ITEM_ID));
                    setFab(getArguments().getInt(ITEM_ID), floatingActionButton);
                    break;

                default:
                    Log.i("RecyclerFragment", "nothing to show");

            }
        }
    }

    private void setFab(int param, FloatingActionButton fab) {
        switch (param) {
            case R.id.navigation_filtered:

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog();
                    }
                });
                break;
            case R.id.navigation_quarantined:
                fab.setVisibility(View.GONE);
                break;
            case R.id.navigation_exceptions:

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }
    }

    private void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View inflateView = layoutInflater.inflate(R.layout.dialog_add_word_view, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_add_word_to_filter)
                .setView(inflateView);

        final EditText getWord = (EditText) inflateView
                .findViewById(R.id.edittext_dialog_add_word);

        dialogBuilder.setPositiveButton(R.string.positive_button_dialog_add_word,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!getWord.getText().toString().equals("")) {
                            new RecyclerViewFragment.UpdateFilteredWords().execute(
                                    getWord.getText().toString());
                        } else {
                            Toast.makeText(context, R.string.toast_empty_string_dialog_add_word,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.negative_button_dialog_add_word,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        dialogBuilder.create();
        dialogBuilder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        items.close();
        db.close();
    }


    private class GetItemsCursor extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            db = helper.getReadableDatabase();

            switch (params[0]) {
                case R.id.navigation_filtered:
                    items = db.query(FilteredWordsTable.TABLE_NAME, null, null, null, null, null,
                            null);
                    break;
                case R.id.navigation_quarantined:
                    items = db.query(QuarantinedTable.TABLE_NAME, null, null, null, null, null,
                            null);
                    break;
                case R.id.navigation_exceptions:
                    items = db.query(ExceptionNamesTable.TABLE_NAME, null, null, null, null, null,
                            null);
                    break;
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(Integer params) {
            switch (params) {
                case R.id.navigation_filtered:
                    recyclerView.setAdapter(new FilteredWordsAdapter(context,
                            items));
                    break;
                case R.id.navigation_quarantined:
                    recyclerView.setAdapter(new QuarantinedAdapter(context,
                            items));
                    break;
                case R.id.navigation_exceptions:
                    recyclerView.setAdapter(new ExceptionsAdapter(context,
                            items));
                    break;
            }

        }
    }

    private class UpdateFilteredWords extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                db = helper.getWritableDatabase();

                items = db.query(FilteredWordsTable.TABLE_NAME, null, FilteredWordsTable.COLUMN_WORD +
                        " = ?", new String[]{params[0]}, null, null, null);


                if (!items.moveToFirst() || items.moveToFirst() && !items.getString(items
                        .getColumnIndex(FilteredWordsTable.COLUMN_WORD)).equals(params[0])) {
                    ContentValues wordToFilter = new ContentValues();
                    wordToFilter.put(FilteredWordsTable.COLUMN_WORD, params[0]);

                    db.insert(FilteredWordsTable.TABLE_NAME, null, wordToFilter);
                    return false;
                }

            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.e("UpdateFilteredWords:", "Can't get access to the DB!");
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean isAdded) {

            if (!isAdded) {
                context.sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_ENTITY_INSERTED));
            } else {
                Toast.makeText(context, R.string.dialog_add_word_already_filtered,
                        Toast.LENGTH_LONG).show();
            }
            db.close();
        }
    }
}
