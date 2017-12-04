package ru.tinted_knight.sberbanksms.SmsReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import ru.tinted_knight.sberbanksms.dao.AppDatabase;
import ru.tinted_knight.sberbanksms.dao.ParseUtils;
import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;

public class SmsReceiverLiveData extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION.equalsIgnoreCase(intent.getAction())) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            AppDatabase database = AppDatabase.getInstance(context);
            // TODO: SmsProcessAsync
            new SmsProcessAsync(database).execute(pdus);
        }
    }

    private static class SmsProcessAsync extends AsyncTask<Object[], Void, Long> {

        private AppDatabase database;

        SmsProcessAsync(AppDatabase database){
            this.database = database;
        }
        
        @Override
        protected Long doInBackground(Object[]... objects) {
            Object[] pdus = objects[0];
            String messageString = ParseUtils.parseFromPdus(pdus);
            FullMessageEntity entity = ParseUtils.fromStringToEntity(messageString);
            if (entity != null)
                database.dao().insertMessage(entity);
            return null;
        }

    }

}
