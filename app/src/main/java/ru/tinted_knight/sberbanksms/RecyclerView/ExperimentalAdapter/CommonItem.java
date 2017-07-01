package ru.tinted_knight.sberbanksms.RecyclerView.ExperimentalAdapter;

import ru.tinted_knight.sberbanksms.Message.Message;

public class CommonItem extends Item {

    public Message mMessage;

    public CommonItem(Message message) {
        mMessage = message;
    }

    @Override
    public Type getType() {
        return Type.Item;
    }

    @Override
    public String getData() {
        return "";
    }

}
