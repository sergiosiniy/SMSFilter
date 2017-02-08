package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.entities.Quarantined;

/**
 * Created by SergioSiniy on 08.02.2017.
 */

public class ExceptionsAdapter extends RecyclerView.Adapter<ExceptionsAdapter.ViewHolder> {

    private List<Quarantined> quarantinedMessages;
    private Context mContext;

    public ExceptionsAdapter(Context context, List<Quarantined> list){
        this.quarantinedMessages=list;
        this.mContext=context;
    }

    @Override
    public ExceptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View quarantinedView = inflater.inflate(R.layout.quarantined_list_item_view, parent, false);

        return new ViewHolder(quarantinedView);
    }

    @Override
    public void onBindViewHolder(ExceptionsAdapter.ViewHolder holder, int position) {
        Quarantined quarantined = quarantinedMessages.get(position);

        TextView phone = holder.phone;
        TextView message = holder.message;

        phone.setText(quarantined.getPhoneNumber());
        message.setText(quarantined.getMessageBody());
    }

    @Override
    public int getItemCount() {
        return quarantinedMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView phone;
        public TextView message;

        public ViewHolder(View itemView){
            super(itemView);

            phone = (TextView) itemView.findViewById(R.id.quarantined_listitem_phone);
            message = (TextView) itemView.findViewById(R.id.quarantined_listitem_message);
        }

    }

    private Context getContext(){
        return mContext;
    }
}
