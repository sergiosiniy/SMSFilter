package ua.kiev.sergiosiniy.smsfilter.activities;


import android.content.Context;
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
import ua.kiev.sergiosiniy.smsfilter.entities.FilteredWord;
import ua.kiev.sergiosiniy.smsfilter.entities.Quarantined;
import ua.kiev.sergiosiniy.smsfilter.utils.DBHelper;
import ua.kiev.sergiosiniy.smsfilter.utils.FilteredWordsAdapter;
import ua.kiev.sergiosiniy.smsfilter.utils.QuarantinedAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {

    private Context context;
    public static final String LIST_TYPE = "list";
    private RecyclerView recyclerView;


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
                    setFab(getArguments().getString(LIST_TYPE), floatingActionButton);
                    recyclerView.setAdapter(new FilteredWordsAdapter(context,
                            FilteredWord.getFilteredList(new DBHelper(context))));
                    break;
                case "Quarantined":
                    setFab(getArguments().getString(LIST_TYPE), floatingActionButton);
                    recyclerView.setAdapter(new QuarantinedAdapter(context,
                            Quarantined.getMessagesList(new DBHelper(context))));
                    break;
                case "Exceptions":
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
}
