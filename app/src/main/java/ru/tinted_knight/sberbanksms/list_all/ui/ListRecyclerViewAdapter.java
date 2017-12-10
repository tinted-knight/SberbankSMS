package ru.tinted_knight.sberbanksms.list_all.ui;

import android.content.Context;
import android.graphics.Color;
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
        void onItemClick(int id);
    }

    public ListRecyclerViewAdapter(Context context, List<SimpleEntity> data, ListItemClickListener listener) {
        this.data = data;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleEntity e = data.get(position);
        holder.tvAgent.setText(e.agent);
        holder.tvSumma.setText(e.getSummaString());
        holder.tvMonth.setText(e.getMonthString());
        holder.tvDay.setText(e.getDayString());

        holder.tvAgent.setTag(e._id);

        switch (e.type) {
            case Constants.OperationType.INCOME:
                holder.tvSumma.setTextColor(ContextCompat.getColor(context, R.color.summa_income));
                break;
            case Constants.OperationType.OUTCOME:
                holder.tvSumma.setTextColor(ContextCompat.getColor(context, R.color.summa_expense));
                break;
            case Constants.OperationType.ATM_OUT:
                holder.tvSumma.setTextColor(ContextCompat.getColor(context, R.color.summa_atm));
                break;
//            default:
//                throw new IllegalArgumentException(":: onBindViewHolder() - message type is incorrect");
        }

        int color = defineDateColor(e.month);
        holder.tvDay.setTextColor(color);
        holder.tvMonth.setTextColor(color);
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
        return data.size();
    }

    public void swapData(List<SimpleEntity> newData) {
        if (newData != null)
            this.data = newData;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAgent;
        private TextView tvSumma;
        private TextView tvMonth;
        private TextView tvDay;

        ViewHolder(View itemView) {
            super(itemView);

            tvAgent = itemView.findViewById(R.id.tvAgent);
            tvSumma = itemView.findViewById(R.id.tvSumma);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvDay = itemView.findViewById(R.id.tvDay);

            tvAgent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sListener.onItemClick((Integer) view.getTag());
                }
            });
        }

    }

}
