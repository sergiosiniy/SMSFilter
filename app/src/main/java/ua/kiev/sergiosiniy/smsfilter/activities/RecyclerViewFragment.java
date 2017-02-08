package ua.kiev.sergiosiniy.smsfilter.activities;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.entities.FilterExceptionName;
import ua.kiev.sergiosiniy.smsfilter.entities.FilteredWord;
import ua.kiev.sergiosiniy.smsfilter.entities.Quarantined;
import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;
import ua.kiev.sergiosiniy.smsfilter.utils.ExceptionsAdapter;
import ua.kiev.sergiosiniy.smsfilter.utils.FilteredWordsAdapter;
import ua.kiev.sergiosiniy.smsfilter.utils.QuarantinedAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {

    private Context context;
    public static final String LIST_TYPE = "list";
    private RecyclerView recyclerView;
    SQLiteOpenHelper helper;
    SQLiteDatabase db;
    private Cursor items;


    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 23) {
            context = getActivity();
        } else {
            context = getContext();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        helper = new DBHelper(context);
        db = helper.getReadableDatabase();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FloatingActionButton floatingActionButton =
                (FloatingActionButton) view.findViewById(R.id.fab_recycler_view);

        if (getArguments().getString(LIST_TYPE) != null) {
            switch (getArguments().getString(LIST_TYPE)) {
                case "Filtered words":
                    new GetItemsCursor().execute("Filtered words");
                    setFab(getArguments().getString(LIST_TYPE), floatingActionButton);

                    break;
                case "Quarantined":
                    new GetItemsCursor().execute("Quarantined");
                    setFab(getArguments().getString(LIST_TYPE), floatingActionButton);

                    break;
                case "Exceptions":
                    new GetItemsCursor().execute("Exceptions");
                    setFab(getArguments().getString(LIST_TYPE), floatingActionButton);
                    break;
                default:
                    Log.i("RecyclerFragment", "nothing to show");

            }
        }

    }

    private void setFab(String param, FloatingActionButton fab) {
        switch (param) {
            case "Filtered words":
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case "Quarantined":
                fab.setVisibility(View.GONE);
                break;
            case "Exceptions":
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
        items.close();
    }

    private class GetItemsCursor extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            switch (params[0]) {
                case "Filtered words":
                    items = db.query(FilteredWord.TABLE_NAME, null, null, null, null, null, null);
                    break;
                case "Quarantined":
                    items = db.query(Quarantined.TABLE_NAME, null, null, null, null, null, null);
                    break;
                case "Exceptions":
                    items = db.query(FilterExceptionName.TABLE_NAME, null, null, null, null, null, null);

                    break;
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(String params) {
            switch (params) {
                case "Filtered words":
                    recyclerView.setAdapter(new FilteredWordsAdapter(context,
                            items));

                    break;
                case "Quarantined":
                    recyclerView.setAdapter(new QuarantinedAdapter(context,
                            items));
                    break;
                case "Exceptions":
                    recyclerView.setAdapter(new ExceptionsAdapter(context,
                            items));
                    break;
            }
        }
    }
}
