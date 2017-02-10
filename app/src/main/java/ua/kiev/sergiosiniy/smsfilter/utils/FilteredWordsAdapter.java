package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.entities.FilteredWord;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilteredWordsAdapter extends RecyclerView.Adapter<FilteredWordsAdapter.ViewHolder> {

    private Cursor filteredWords;
    private Context mContext;

    public FilteredWordsAdapter(Context context, Cursor list) {
        this.filteredWords = list;
        this.mContext = context;
    }

    @Override
    public FilteredWordsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View wordsView = inflater.inflate(R.layout.filtered_list_item_view, parent, false);

        return new ViewHolder(wordsView);
    }

    @Override
    public void onBindViewHolder(FilteredWordsAdapter.ViewHolder holder, int position) {
        filteredWords.moveToPosition(position);
        FilteredWord filtered = new FilteredWord(filteredWords.getInt(0),
                filteredWords.getString(1));

        TextView word = holder.word;
        word.setText(filtered.getPhoneNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

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
}
