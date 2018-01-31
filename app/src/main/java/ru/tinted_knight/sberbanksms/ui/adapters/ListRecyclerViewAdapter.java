package ru.tinted_knight.sberbanksms.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private List<SimpleEntity> data;

    private static ListItemClickListener sListener;

    private Context context;

    public interface ListItemClickListener {
        void onItemClick(int id, ViewHolder holder);

        void onItemLongClick(int id);
    }

    public ListRecyclerViewAdapter(Context context, ListItemClickListener listener) {
        this.context = context;
        if (listener != null)
            sListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.message_card3, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SimpleEntity e = data.get(position);
        if (e.alias != null && !"".equals(e.alias))
            holder.agent.setText(e.alias);
        else
            holder.agent.setText(e.agent);

        holder.summa.setText(e.getSummaString());
        holder.month.setText(e.getMonthString());
        holder.day.setText(e.getDayString());

        holder.itemView.setTag(e._id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.agent.setTransitionName("agent" + position);
            holder.summa.setTransitionName("summa" + position);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sListener.onItemClick(e._id, holder);
            }
        });

        switch (e.type) {
            case Constants.OperationType.INCOME:
                holder.summa.setTextColor(ContextCompat.getColor(context, R.color.summa_income));
                break;
            case Constants.OperationType.OUTCOME:
                holder.summa.setTextColor(ContextCompat.getColor(context, R.color.summa_expense));
                break;
            case Constants.OperationType.ATM_OUT:
                holder.summa.setTextColor(ContextCompat.getColor(context, R.color.summa_atm));
                break;
//            default:
//                throw new IllegalArgumentException(":: onBindViewHolder() - message type is incorrect");
        }

        int color = defineDateColor(e.month);
        holder.day.setTextColor(color);
        holder.month.setTextColor(color);
    }

    private int defineDateColor(int month) {
        switch (month) {
            case 1:
                return ContextCompat.getColor(context, R.color.january);
            case 2:
                return ContextCompat.getColor(context, R.color.february);
            case 3:
                return ContextCompat.getColor(context, R.color.march);
            case 4:
                return ContextCompat.getColor(context, R.color.april);
            case 5:
                return ContextCompat.getColor(context, R.color.may);
            case 6:
                return ContextCompat.getColor(context, R.color.june);
            case 7:
                return ContextCompat.getColor(context, R.color.july);
            case 8:
                return ContextCompat.getColor(context, R.color.august);
            case 9:
                return ContextCompat.getColor(context, R.color.september);
            case 10:
                return ContextCompat.getColor(context, R.color.october);
            case 11:
                return ContextCompat.getColor(context, R.color.november);
            case 12:
                return ContextCompat.getColor(context, R.color.december);
            default:
                return Color.parseColor("#E65100");
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    public void setData(List<SimpleEntity> newData) {
        if (newData != null)
            this.data = newData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView agent;
        public TextView summa;
        public TextView month;
        public TextView day;

        ViewHolder(View itemView) {
            super(itemView);

            agent = itemView.findViewById(R.id.tvAgent);
            summa = itemView.findViewById(R.id.tvSumma);
            month = itemView.findViewById(R.id.tvMonth);
            day = itemView.findViewById(R.id.tvDay);
        }

    }
}
