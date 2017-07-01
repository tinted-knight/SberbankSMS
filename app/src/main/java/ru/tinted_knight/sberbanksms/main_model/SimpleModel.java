package ru.tinted_knight.sberbanksms.main_model;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Telephony;

import java.text.ParseException;
import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Agents;
import ru.tinted_knight.sberbanksms.Message.Cards;
import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.MessageProcessor;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.RawProcessor;
import ru.tinted_knight.sberbanksms.Message.MessageReader.DeviceInboxCursorMessageReader;
import ru.tinted_knight.sberbanksms.Settings.Preferences;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;


public class SimpleModel implements ISimpleModel {

    private OnProgressUpdateListener mListener;

    @Override
    public int loadDeviceSms(Context context, OnProgressUpdateListener listener) {
        Cursor cursor = new DeviceInboxCursorMessageReader(context).read();
        mListener = listener;
        AsyncSmsSQLiteWriter parser = new AsyncSmsSQLiteWriter(context);
        parser.execute(cursor);
        return cursor.getCount();
    }

    @Override
    public List<Message> loadFromDatabase() {
        return null;
    }

    private class AsyncSmsSQLiteWriter extends AsyncTask<Cursor, Integer, Void> {

        private Cursor cursor;
        private int progress = 1;
        private final Context context;

        AsyncSmsSQLiteWriter(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Cursor... params) {
            this.cursor = params[0];
            try {
                if (cursor.moveToLast()) {
                    int indexBody = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
                    int indexDate = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE);
                    do {
                        MessageProcessor messageProcessor =
                                new MessageProcessor(context, MessageContentProvider.UriFullSms,
                                        new Message(cursor.getString(indexBody), cursor.getLong(indexDate)));
                        long rowId = messageProcessor.save();
                        if (rowId != -1 && rowId != 0) {
                            RawProcessor rawProcessor =
                                    new RawProcessor(context, MessageContentProvider.UriRawSms,
                                            cursor.getString(indexBody));
                            rawProcessor.save(rowId);
                        }
                        publishProgress(progress++);
                    } while (cursor.moveToPrevious());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Preferences.setFirtsRun(context, "Main2", false);
            // TODO: context может стать null
            AsyncAgentListWriter agentListWriter = new AsyncAgentListWriter(context);
            agentListWriter.execute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mListener.onProgressUpdate(values[0]);
        }

    }

    private class AsyncAgentListWriter extends AsyncTask<Void, Integer, Void> {

        private Context context;
        private Cursor cursor;
        private int progress = 1;

        AsyncAgentListWriter(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cursor = context.getContentResolver().query(MessageContentProvider.UriAgentsInitial, null, null, null, null);
            mListener.onProgressUpdate("Запись в базу данных", "Спасибо за терпение!", cursor.getCount());
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (cursor.moveToFirst()) {
                Agents agents = new Agents(context, cursor.getCount());
                do {
                    agents.add(cursor.getString(cursor.getColumnIndex(DBHandler.MessagesTable.Agent)));
                    publishProgress(progress++);
                } while (cursor.moveToNext());
                agents.save();
            }
            cursor.close();
            Cards.extractCards(context);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mListener.onProgressUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mListener.onProgressDone();
        }
    }


}
