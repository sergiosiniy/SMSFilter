package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.entities.FilteredWord;
import ua.kiev.sergiosiniy.smsfilter.entities.Quarantined;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class FilteredWordsAdapter extends RecyclerView.Adapter<FilteredWordsAdapter.ViewHolder> {

    private List<FilteredWord> filteredWords;
    private Context mContext;

    public FilteredWordsAdapter(Context context, List<FilteredWord> list) {
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
        FilteredWord filtered = filteredWords.get(position);

        TextView word = holder.word;

        word.setText(filtered.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return filteredWords.size();
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