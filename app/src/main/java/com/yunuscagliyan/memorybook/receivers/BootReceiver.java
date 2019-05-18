package com.yunuscagliyan.memorybook.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yunuscagliyan.memorybook.services.NotificationService;

import static android.content.Context.ALARM_SERVICE;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager manager= (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent intent1=new Intent(context, NotificationService.class);
        PendingIntent pendingIntent=PendingIntent.getService(context,100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,1000,5000,pendingIntent);
    }
}
