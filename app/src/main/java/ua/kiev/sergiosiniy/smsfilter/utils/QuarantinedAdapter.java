package ua.kiev.sergiosiniy.smsfilter.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.entities.Quarantined;
import ua.kiev.sergiosiniy.smsfilter.tables.FilteredWordsTable;

/**
 * Created by SergioSiniy on 07.02.2017.
 */

public class QuarantinedAdapter extends RecyclerView.Adapter<QuarantinedAdapter.ViewHolder> {

    private Cursor quarantinedMessages;
    private Context mContext;

    public QuarantinedAdapter(Context context, Cursor list) {
        this.quarantinedMessages = list;
        this.mContext = context;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DatabaseChangedReceiver.ACTION_ENTITY_INSERTED);
        intentFilter.addAction(DatabaseChangedReceiver.ACTION_ENTITY_DELETED);

        DatabaseChangedReceiver changesReceiver = new DatabaseChangedReceiver() {
            @Override
            public void onReceive(Context receiveContext, Intent intent) {
                new QuarantinedAdapter.UpdateCursor().execute(intent);
            }
        };
        context.registerReceiver(changesReceiver, intentFilter);
    }

    @Override
    public QuarantinedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View quarantinedView = inflater.inflate(R.layout.quarantined_list_item_view, parent, false);

        return new ViewHolder(quarantinedView);
    }

    @Override
    public void onBindViewHolder(QuarantinedAdapter.ViewHolder holder, int position) {
        quarantinedMessages.moveToPosition(position);
        Quarantined quarantined = new Quarantined(quarantinedMessages.getInt(0),
                quarantinedMessages.getString(1), quarantinedMessages.getString(2));

        TextView phone = holder.phone;
        TextView message = holder.message;

        phone.setText(quarantined.getPhoneNumber());
        message.setText(quarantined.getMessageBody());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return quarantinedMessages.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView phone;
        public TextView message;

        public ViewHolder(View itemView) {
            super(itemView);

            phone = (TextView) itemView.findViewById(R.id.quarantined_listitem_phone);
            message = (TextView) itemView.findViewById(R.id.quarantined_listitem_message);
        }

    }

    private Context getContext() {
        return mContext;
    }

    private class UpdateCursor extends AsyncTask<Intent, Void, Intent> {
        SQLiteOpenHelper helper = new DBHelper(mContext);
        SQLiteDatabase db;

        @Override
        protected Intent doInBackground(Intent... params) {

            db = helper.getReadableDatabase();
            quarantinedMessages.close();
            quarantinedMessages = db.query(FilteredWordsTable.TABLE_NAME, null, null, null, null, null, null);
            return params[0];
        }

        @Override
        protected void onPostExecute(Intent intent) {
            switch (intent.getAction()) {
                case DatabaseChangedReceiver.ACTION_ENTITY_INSERTED:
                    notifyItemInserted(getItemCount());
                    notifyItemRangeChanged(getItemCount(), getItemCount());
                    break;
                /*case DatabaseChangedReceiver.ACTION_ENTITY_DELETED:
                    notifyItemRemoved(listItemDeletePosition);
                    notifyItemRangeChanged(listItemDeletePosition,getItemCount());
                    break;*/
            }
            db.close();

        }
    }
}
