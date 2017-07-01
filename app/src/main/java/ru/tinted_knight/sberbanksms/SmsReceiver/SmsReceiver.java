package ru.tinted_knight.sberbanksms.SmsReceiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;

import java.text.ParseException;
import java.util.HashSet;

import ru.tinted_knight.sberbanksms.MainOld;
import ru.tinted_knight.sberbanksms.Message.Agents;
import ru.tinted_knight.sberbanksms.Message.Cards;
import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.MessageProcessor;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.RawProcessor;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Constants.OperationType;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;

public class SmsReceiver extends BroadcastReceiver {

    private static final String Action = "android.provider.Telephony.SMS_RECEIVED";
    private static final int NOTIFY_ID = 1;
    private int mOperationType;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && Action.equalsIgnoreCase(intent.getAction())) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            final SmsMessage[] messages = new SmsMessage[pdus.length];
//            String format = intent.getStringExtra("format");
            for (int i = 0; i < pdus.length; i++) {
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
//                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
//                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
//            }
            String smsFrom = messages[0].getDisplayOriginatingAddress();
            long dateReceived = messages[0].getTimestampMillis();
            if (smsFrom.equalsIgnoreCase(Constants.SBER_PHONE_NUMBER)) {
                StringBuilder rawBody = new StringBuilder();
                for (SmsMessage message : messages) {
                    rawBody.append(message.getMessageBody());
                }
                SmsProcessAsync smsProcessTask = new SmsProcessAsync(context, dateReceived);
                smsProcessTask.execute(String.valueOf(rawBody));
            }
        }
    }

    private class SmsProcessAsync extends AsyncTask<String, Void, Long> {

        private Context context;

        long dateReceived;

        SmsProcessAsync(Context context, long dateReceived) {
            super();
            this.context = context;
            this.dateReceived = dateReceived;
        }

        @Override
        protected Long doInBackground(String... params) {
//            Slog.log("doInBackground");
            long length = 0;
            String rawBody = params[0];
            try {
                Message m = new Message(rawBody, dateReceived);
                MessageProcessor messageProcessor = new MessageProcessor(context, MessageContentProvider.UriFullSms, m);
                long result = messageProcessor.save();
                if (result != -1) {
                    String agent = m.getAgent();
                    if (Agents.isUnique(context, agent)) {
                        Agents.insert(context, agent);
                    }
                    RawProcessor rawProcessor = new RawProcessor(context, MessageContentProvider.UriRawSms, rawBody);
                    rawProcessor.save(result);
                    String card = m.getCard();
                    if (Cards.isUnique(context, card)) {
                        HashSet<String> cardToSave = new HashSet<>();
                        cardToSave.add(card);
                        Cards.save(context, cardToSave);
                    }
                    mOperationType = m.getType();
                    return result;
                } else {
                    return -1L;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return length;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Slog.log("SmsReceiver START");
        }

        @Override
        protected void onPostExecute(Long rowId) {
            super.onPostExecute(rowId);
//            Slog.log("onPostExecute");
            Intent responsibleActivity = new Intent(context, MainOld.class);
            PendingIntent responsibleIntent = PendingIntent.getActivity(context, 0, responsibleActivity, 0);

//            String text = rowId == -1 ? "Странная СМС-ка" : "Возможно, Вы покупаете что-то прямо сейчас :(";
            String displayedMessage;
            String displayedTitle;
            switch (mOperationType) {
                case OperationType.INCOME:
                    displayedMessage = "Йо-хо-хо, пора тратить заработанное ;)";
                    displayedTitle = "Уряяяя!!!";
                    break;
                case OperationType.OUTCOME:
                    displayedMessage = "Возможно, Вы покупаете что-то прямо сейчас :(";
                    displayedTitle = "Опасно!!!";
                    break;
                default:
                    displayedMessage = "грусть-тоска :(";
                    displayedTitle = "грусть-тоска :(";
                    break;
            }
            String text = rowId == -1 ? "Я в замешательстве, не понимаю..." : displayedMessage;
            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_shopping_basket)
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_shopping_basket))
                    .setTicker("O_O Что-то происходит...")
                    .setContentTitle(displayedTitle)
                    .setContentText(text)
                    .setContentIntent(responsibleIntent)
                    //TODO:  проверить настройку автоотмены уведомления
                    .setAutoCancel(true)
                    .build();
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.notify(NOTIFY_ID, notification);

            Intent intent = new Intent(Constants.BroadcastIncomeSms);
            intent.putExtra("status", 1);
            intent.putExtra("rowId", rowId);
            context.sendBroadcast(intent);

//            Slog.log("SmsReceiver STOP, SMS rowId = " + rowId);
        }
    }

}
