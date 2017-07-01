package ru.tinted_knight.sberbanksms.Message;

public class Card {

    private String mCard;
    private int mId;

    Card(String card, int id) {
        mCard = card;
        mId = id;
    }

    public String getCard() {
        return mCard;
    }

    public int getId() {
        return mId;
    }
}

