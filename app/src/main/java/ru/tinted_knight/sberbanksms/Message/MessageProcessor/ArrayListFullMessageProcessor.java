package ru.tinted_knight.sberbanksms.Message.MessageProcessor;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageBuilder;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;

/*
    Читает сообщения из базы данных и строит ArrayList<Message> для обработки Адаптером
 */
public class ArrayListFullMessageProcessor extends CursorMessageProcessor<ArrayList<Message>> {

    public ArrayListFullMessageProcessor(Cursor cursor, Context context) {
        super(cursor, context);
    }

    @Override
    public ArrayList<Message> process() {
        ArrayList<Message> messages = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                long _id = cursor.getLong(cursor.getColumnIndex(DBHandler.MessagesTable.Id));
                int type = cursor.getInt(cursor.getColumnIndex(DBHandler.MessagesTable.Type));
                if (type == Constants.OperationType.DEBUG) continue;
                String card = cursor.getString(cursor.getColumnIndex(DBHandler.MessagesTable.Card));
                Constants.CardType cardType = Constants.CardType.valueOf(
                        cursor.getString(cursor.getColumnIndex(DBHandler.MessagesTable.CardType)));
                int date = cursor.getInt(cursor.getColumnIndex(DBHandler.MessagesTable.Date));
                String agent = cursor.getString(cursor.getColumnIndex(DBHandler.MessagesTable.Agent));
                Float summa = cursor.getFloat(cursor.getColumnIndex(DBHandler.MessagesTable.Summa));
                Float balance = cursor.getFloat(cursor.getColumnIndex(DBHandler.MessagesTable.Balance));
//                String raw = cursor.getString(cursor.getColumnIndex(DBHandler.MessagesTable.Raw));
                String alias = cursor.getString(cursor.getColumnIndex(DBHandler.AgentsAliases.Alias));

                if (agent.equals("")){
                    switch (type) {
                        case Constants.OperationType.INCOME:
                            agent = context.getString(R.string.agent_unrecognized_income);
                            break;
                        case Constants.OperationType.OUTCOME:
                            agent = context.getString(R.string.agent_unrecognized_outcome);
                            break;
                    }
                }

                if (alias == null || alias.equals("")) {
                    // smth
                }

                Message message = new MessageBuilder()
                        ._id(_id)
                        .card(card)
                        .cardType(cardType)
                        .date(date)
                        .type(type)
                        .agent(agent)
                        .summa(summa)
                        .balance(balance)
//                        .raw(raw)
                        .alias(alias)
                        .build();
                messages.add(message);
            } while (cursor.moveToNext());
            return messages;
        }
        return null;
    }

}
