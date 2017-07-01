package ru.tinted_knight.sberbanksms.RecyclerView.ExperimentalAdapter;

public class YearItem extends Item {

    public String mYear;

    public YearItem(String year) {
        mYear = year;
    }

    @Override
    public Type getType() {
        return Type.Header;
    }

    @Override
    public String getData() {
        return mYear;
    }

}
