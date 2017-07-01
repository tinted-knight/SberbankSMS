package ru.tinted_knight.sberbanksms.RecyclerView.ExperimentalAdapter;

public class MonthItem extends Item {

    public String mMonth;

    public MonthItem(String year) {
        mMonth = year;
    }

    @Override
    public Type getType() {
        return Type.Header;
    }

    @Override
    public String getData() {
        return mMonth;
    }

}
