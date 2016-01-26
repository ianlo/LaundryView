package ianlo.net.cmulaundry;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;

import ui.MainActivity;

/**
 * Created by ianlo on 2016-01-11.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Laundry Cycle Finished")
                .setContentText("Go pick up your laundry!")
                .setSmallIcon(R.drawable.ic_stat)
                .setVibrate(new long[] {0, 300, 200, 300})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
                .build();

        notificationManager.notify(0, notification);
        // Get the PendingIntent and then cancel the scheduled notification.
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        alarmMgr.cancel(alarmIntent);
        if (alarmIntent != null) {
            alarmIntent.cancel();
        }
    }
}

