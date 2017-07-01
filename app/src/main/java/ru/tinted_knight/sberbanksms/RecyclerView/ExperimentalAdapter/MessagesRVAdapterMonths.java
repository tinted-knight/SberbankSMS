package ru.tinted_knight.sberbanksms.RecyclerView.ExperimentalAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.Messages;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Slog;


public class MessagesRVAdapterMonths extends RecyclerView.Adapter<MessagesRVAdapterMonths.ViewHolder> {

    private List<Message> mMessages;
    private List<Message> mSrcMessages;
    private Map<Integer, Map<Integer, ArrayList<Message>>> mMap;
    private List<Item> mItems;
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
        MessagesRVAdapterMonths.sListenerLong = listenerLong;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        MessagesRVAdapterMonths.sListener = listener;
    }

    public MessagesRVAdapterMonths(Context context, List<Message> data) throws ParseException {
        if (context != null) {
            mContext = context;
            mMessages = data;
            buildMap();
            buildItems();
        } else {
            Slog.log("MessRVAdapter mContext = null");
        }
    }

    private void buildItems() {
        mItems = new ArrayList<>();
//        for (Integer year : mMap.keySet()) {
        for (Integer year = 2017; year >= 2015; year--) {
            Item headerItem = new YearItem(year.toString());
            mItems.add(headerItem);
            for (Integer month = 12; month >= 1; month--) {
                if (!mMap.get(year).get(month).isEmpty()) {
                    Item monthItem = new MonthItem(month.toString());
                    mItems.add(monthItem);
                    for (Message m : mMap.get(year).get(month)) {
                        Item commonItem = new CommonItem(m);
                        mItems.add(commonItem);
                    }
                }
            }
        }
    }

    private void buildMap() {
        mMap = new TreeMap<>();
        LinkedList<Integer> years = Messages.getYears(mContext);
        for (Integer y : years) {
            mMap.put(y, new TreeMap<Integer, ArrayList<Message>>());
            for (int i = 1; i <= 12; i++) {
                mMap.get(y).put(i, new ArrayList<Message>());
            }
        }
        for (Message m : mMessages) {
            Integer year = m.getYear();
            Integer month = m.getMonth();
            mMap.get(year).get(month).add(m);
        }
    }

    public Constants.CardType getCardType(int i) {
        return mMessages.get(i).getCardType();
    }

    public float getCurrentBalance() {
        return mMessages.get(mMessages.size() - 1)
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
//        View messageView = inflater.inflate(R.layout.message_card, parent, false);

        return new ViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mItems.get(position);
        switch (item.getType()) {
            case Header:
                holder.tvAgent.setText(item.getData());
                holder.tvDay.setText("");
                holder.tvMonth.setText("");
                holder.tvSumma.setText("");
                break;
            case Item:
                Message message = ((CommonItem) item).mMessage;
                String[] dates = message.getDateShort().split(" ");
                holder.tvDay.setText(dates[0]);
                holder.tvMonth.setText(dates[1]);

                holder.tvDay.setTextColor(Color.parseColor("#E65100"));
                holder.tvMonth.setTextColor(Color.parseColor("#E65100"));
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
                break;
        }
    }

    @Override
    public int getItemCount() {
//        if (mMessages != null) {
//            return mMessages.size();
//        }
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSumma;
        //        TextView tvDate;
        TextView tvDay;
        TextView tvMonth;
        TextView tvAgent;
        ImageView ivOperationIcon;

        ViewHolder(View itemView) {
            super(itemView);

            tvSumma = (TextView) itemView.findViewById(R.id.tvSumma);
//            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
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

//                tvDate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (sListener != null) {
//                            sListener.onItemClick(v, getLayoutPosition());
//                        }
//                    }
//                });
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

//            tvDate.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (sListenerLong != null) {
//                        sListenerLong.onLongClick(v, getLayoutPosition());
//                        return true;
//                    }
//                    return false;
//                }
//            });
        }

    }

}
