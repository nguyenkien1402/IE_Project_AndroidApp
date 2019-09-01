package com.mobile.tiamo.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.mobile.tiamo.dao.DailyActivities;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;

public class NotificationActionBroadcastReceiver extends BroadcastReceiver {

    String action = null;
    TiamoDatabase db;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        long uid = intent.getIntExtra("uid",-1);
        db = SQLiteDatabase.getTiamoDatabase(context);
        action = intent.getAction();
        if(action.equals("YES_ACTION")){
            Log.d("Action","Yes:"+uid);
        }
        if(action.equals("NO_ACTION")){
            Log.d("Action","No:"+uid);
        }
        updateToDatabase(uid);
        nm.cancel(2);
    }

    private void updateToDatabase(long uid){
        UpdateActionToDatabaseAsync updateActionToDatabaseAsync = new UpdateActionToDatabaseAsync();
        updateActionToDatabaseAsync.execute(uid);
    }

    private class UpdateActionToDatabaseAsync extends AsyncTask<Long, Void, Void>{
        @Override
        protected Void doInBackground(Long... longs) {
            DailyActivities dailyActivities = db.dailyActivitiesDao().getDailyActivityById(longs[0]);
            if(action.equals("YES_ACTION")){
                dailyActivities.setIsDone(1);
                db.dailyActivitiesDao().update(dailyActivities);
            }

            if(action.equals("NO_ACTION")){
                dailyActivities.setIsDone(0);
                db.dailyActivitiesDao().update(dailyActivities);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }




}
