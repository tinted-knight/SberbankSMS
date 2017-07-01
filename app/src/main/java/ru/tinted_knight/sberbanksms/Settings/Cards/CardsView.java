package ru.tinted_knight.sberbanksms.Settings.Cards;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Card;

interface CardsView {

    void setItems(List<String> items);

    void showSnack(String text);

}
