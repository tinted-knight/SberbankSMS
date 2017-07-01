package ru.tinted_knight.sberbanksms.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Slog;


public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> {

    private List<Message> mMessages;
    private List<Message> mSrcMessages;
    private boolean mFilteredFlag = false;
    private Context mContext;
    private static OnItemClickListener sListener;
    private static OnItemLongClickListener sListenerLong;

    public interface OnItemClickListener {
        void onItemClick(View item, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View item, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listenerLong) {
        MessagesRecyclerViewAdapter.sListenerLong = listenerLong;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        MessagesRecyclerViewAdapter.sListener = listener;
    }

    public MessagesRecyclerViewAdapter(Context context, List<Message> data) throws ParseException {
        if (context != null) {
            mContext = context;
            mMessages = data;
        } else {
            Slog.log("MessRVAdapter mContext = null");
        }
    }

    public Constants.CardType getCardType(int i) {
        return mMessages.get(i).getCardType();
    }

    public float getCurrentBalance() {
        return mMessages.get(0/*mMessages.size()-1*/)
                .getBalance();
    }

    public long get_id(int i) {
        return mMessages.get(i).get_id();
    }

    public int getType(int i) {
        return mMessages.get(i).getType();
    }

    public String getAgent(int i) {
        return mMessages.get(i).getAgent();
    }

    public String getDateFull(int i) {
        return mMessages.get(i).getDateFull();
    }

    public void clearFilter() {
        if (mSrcMessages != null) {
            mMessages = mSrcMessages;
            mSrcMessages = null;
            mFilteredFlag = false;
        }
    }

    public void addTypeFilter(final int type) {
        List<Message> filtered = new ArrayList<>();
        for (Message m : mMessages) {
            if (m.getType() == type) filtered.add(m);
        }
        swapLists(filtered);
    }

    public void addAgentFilter(final String agent) {
        List<Message> filtered = new ArrayList<>();
        for (Message m : mMessages) {
            if (m.getAgent().equalsIgnoreCase(agent)) filtered.add(m);
        }
        swapLists(filtered);
    }

    public void addCardFilter(final String card) {
        List<Message> filtered = new ArrayList<>();
        for (Message m : mMessages) {
            if (m.getCard().equalsIgnoreCase(card)) filtered.add(m);
        }
        swapLists(filtered);
    }

    private void swapLists(List<Message> filtered) {
        if (filtered.size() > 0) {
            if (!mFilteredFlag) {
                mSrcMessages = mMessages;
                mFilteredFlag = true;
            }
            mMessages = filtered;
        }
    }

    public List<Message> swapCursor(List<Message> newData) {
//        Slog.log("swapCursor");
        if (newData == mMessages) {
            return null;
        }
        if (newData != null) {
            mMessages = newData;
            notifyDataSetChanged();
        }
        return mMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View messageView = inflater.inflate(R.layout.message_card3, parent, false);

        return new ViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);

        String[] dates = message.getDateShort().split(" ");
        holder.tvDay.setText(dates[0]);
        holder.tvMonth.setText(dates[1]);

        int color = Color.parseColor("#E65100");
        switch (message.getMonth()) {
            case 1: color = ContextCompat.getColor(mContext, R.color.january); break;
            case 2: color = ContextCompat.getColor(mContext, R.color.february); break;
            case 3: color = ContextCompat.getColor(mContext, R.color.march); break;
            case 4: color = ContextCompat.getColor(mContext, R.color.april); break;
            case 5: color = ContextCompat.getColor(mContext, R.color.may); break;
            case 6: color = ContextCompat.getColor(mContext, R.color.june); break;
            case 7: color = ContextCompat.getColor(mContext, R.color.july); break;
            case 8: color = ContextCompat.getColor(mContext, R.color.august); break;
            case 9: color = ContextCompat.getColor(mContext, R.color.september); break;
            case 10: color = ContextCompat.getColor(mContext, R.color.october); break;
            case 11: color = ContextCompat.getColor(mContext, R.color.november); break;
            case 12: color = ContextCompat.getColor(mContext, R.color.december); break;
        }
        holder.tvDay.setTextColor(color);
        holder.tvMonth.setTextColor(color);

        if (message.getAlias() != null && !message.getAlias().equals("")) {
            holder.tvAgent.setText(message.getAlias());
        } else {
            holder.tvAgent.setText(message.getAgent());
        }

        int type = message.getType();
        switch (type) {
            case Constants.OperationType.INCOME:
                holder.tvSumma.setTextColor(Color.parseColor("#1B5E20"));
                holder.tvSumma.setText(String.format(Locale.getDefault(), "+ %1$,.2f", message.getSumma()));
                break;
            case Constants.OperationType.OUTCOME:
                holder.tvSumma.setTextColor(Color.parseColor("#D50000"));
                holder.tvSumma.setText(String.format(Locale.getDefault(), "- %1$,.2f", message.getSumma()));
                break;
            case Constants.OperationType.ATM_OUT:
                holder.tvSumma.setTextColor(Color.parseColor("#0D47A1"));
                holder.tvSumma.setText(String.format(Locale.getDefault(), "%1$,.2f", message.getSumma()));
                break;
            case Constants.OperationType.DEBUG:
                holder.tvSumma.setTextColor(Color.parseColor("#7A7A7A"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mMessages != null) {
            return mMessages.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSumma;
        TextView tvDay;
        TextView tvMonth;
        TextView tvAgent;
        ImageView ivOperationIcon;

        ViewHolder(View itemView) {
            super(itemView);

            tvSumma = (TextView) itemView.findViewById(R.id.tvSumma);
            tvAgent = (TextView) itemView.findViewById(R.id.tvAgent);
            ivOperationIcon = (ImageView) itemView.findViewById(R.id.ivOperationIcon);

            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);

            if (sListener != null) {
                tvSumma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sListener.onItemClick(v, getLayoutPosition());
                    }
                });

                ivOperationIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sListener != null) {
                            sListener.onItemClick(v, getLayoutPosition());
                        }
                    }
                });

                tvAgent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sListener != null) {
                            sListener.onItemClick(v, getLayoutPosition());
                        }
                    }
                });
            }

            ivOperationIcon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (sListenerLong != null) {
                        sListenerLong.onLongClick(v, getLayoutPosition());
                        return true;
                    }
                    return false;
                }
            });
        }

    }

}
