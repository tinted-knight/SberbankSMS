package ru.tinted_knight.sberbanksms.Settings.Cards;

import android.content.Context;
import android.view.MenuItem;

import ru.tinted_knight.sberbanksms.Message.Cards;

public class SimpleCardPresenter implements CardsPresenter {

    private CardsView mView;
    private Context mContext;

    public SimpleCardPresenter(Context context, CardsView cardsView) {
        mContext = context;
        mView = cardsView;
    }

    @Override
    public void onResume() {
        mView.setItems(Cards.getCardsList(mContext).getStringArrayList());
    }

    @Override
    public void onItemClicked(int position) {
        mView.showSnack("You chosed card at position: " + position);
    }

}
