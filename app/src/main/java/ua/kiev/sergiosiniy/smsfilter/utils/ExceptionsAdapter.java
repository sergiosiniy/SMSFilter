package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.entities.FilterException;
import ua.kiev.sergiosiniy.smsfilter.entities.FilterExceptionName;
import ua.kiev.sergiosiniy.smsfilter.entities.Quarantined;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class ExceptionsAdapter extends RecyclerView.Adapter<ExceptionsAdapter.ViewHolder> {

    private List<FilterExceptionName> exceptedPhonesNames;
    private Context mContext;

    public ExceptionsAdapter(Context context, List<FilterExceptionName> names) {
        this.exceptedPhonesNames = names;
        this.mContext = context;
    }

    @Override
    public ExceptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View quarantinedView = inflater.inflate(R.layout.exceptions_list_item_view, parent, false);

        return new ViewHolder(quarantinedView);
    }

    @Override
    public void onBindViewHolder(ExceptionsAdapter.ViewHolder holder, int position) {
        FilterExceptionName exceptedNameData = exceptedPhonesNames.get(position);

        TextView name = holder.exceptedName;
        TextView date = holder.date;

        name.setText(exceptedNameData.getExceptedPhoneName());
        date.setText(exceptedNameData.getDate());
    }

    @Override
    public int getItemCount() {
        return exceptedPhonesNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView exceptedName;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            exceptedName = (TextView) itemView.findViewById(R.id.excepted_list_name);
            date = (TextView) itemView.findViewById(R.id.excepted_list_date);
        }

    }

    private Context getContext() {
        return mContext;
    }
}
