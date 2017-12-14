package ru.tinted_knight.sberbanksms.Tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;
import ru.tinted_knight.sberbanksms.ui.main_screen.MainActivity;

public class NotificationUtils {

    private static final int NOTIF_ID = 100;

    private static final String NOTIF_CHANNEL_ID = "crazy_shopping_channel_id";

    private static class NotifContent {
        private static String message;
        private static String title;
        private static boolean notifFlag;

        private static void build(Context context, int type, float summa){
            switch (type) {
                case Constants.OperationType.INCOME:
                    message = context.getString(R.string.notif_message_income);
                    title = String.valueOf(summa);
                    notifFlag = true;
                    break;
                case Constants.OperationType.OUTCOME:
                    message = context.getString(R.string.notif_message_expense);
                    title = String.valueOf(summa);
                    notifFlag = true;
                    break;
                case Constants.OperationType.ATM_OUT:
                    message = context.getString(R.string.notif_message_atm);
                    title = String.valueOf(summa);
                    notifFlag = true;
                    break;
                default:
                    message = "default";
                    title = "default";
                    break;
            }
        }
    }

    public static void showSmsReceived(Context context, FullMessageEntity entity) {
        if (entity != null) {
            Intent responsibleActivity = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, responsibleActivity, 0);

            NotifContent.build(context, entity.type, entity.summa);

            if (NotifContent.notifFlag) {
                NotificationManager manager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            NOTIF_CHANNEL_ID,
                            "crazy shopping channel name",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    manager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_shopping_basket)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_shopping_basket))
                        .setTicker("Operation...")
                        .setContentTitle(NotifContent.title)
                        .setContentText(NotifContent.message)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                        && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                }

                manager.notify(NOTIF_ID, builder.build());
            }
        }
    }

}
