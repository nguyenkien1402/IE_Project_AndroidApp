package com.mobile.tiamo.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationActionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String action = intent.getAction();
        if(action.equals("YES_ACTION")){
            Log.d("Action","Yes");
            nm.cancel(2);
        }
        if(action.equals("NO_ACTION")){
            Log.d("Action","No");
            nm.cancel(2);
        }
    }
}
