package ru.tinted_knight.sberbanksms.Message;

import java.util.ArrayList;

public class CardsList {

    private ArrayList<Card> mCards;
    private int mActive = 0;

    public CardsList() {
        mCards = new ArrayList<>();
    }

    public void add(Card card) {
        mCards.add(card);
    }

    public ArrayList<String> getStringArrayList() {
        ArrayList<String> result = new ArrayList<>();
        for (Card c : mCards) {
            result.add(c.getCard());
        }
        return result;
    }

    public void setActive(int active) {
        mActive = active;
    }

    public int getActiveId() {
        return mCards.get(mActive).getId();
    }

    public String getActiveCard() {
        return mCards.get(mActive).getCard();
    }
}
