package com.mobile.tiamo.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.tiamo.R;
import com.mobile.tiamo.dao.DailyActivities;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.util.List;

public class DailyActivityAdapter extends ArrayAdapter<DailyActivityItem> implements View.OnClickListener {

    private List<DailyActivityItem> datasets;
    Context context;
    TiamoDatabase db;
    DailyActivityItem dailyActivityItem;
    ViewHolder viewHolder;
    public static class ViewHolder{
        TextView txtTile, txtHour;
        Switch aSwitch;
    }

    public DailyActivityAdapter(List<DailyActivityItem> datasets, Context context){
        super(context, R.layout.item_dailyactivity, datasets);
        this.datasets = datasets;
        this.context = context;
        db = SQLiteDatabase.getTiamoDatabase(context);
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DailyActivityItem dailyActivityItem = getItem(position);
//        final ViewHolder viewHolder;
        this.dailyActivityItem = dailyActivityItem;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_dailyactivity, parent, false);
            viewHolder.txtTile = (TextView) convertView.findViewById(R.id.item_dailyactivity_title);
            viewHolder.txtHour = (TextView) convertView.findViewById(R.id.item_dailyactivity_hour);
            viewHolder.aSwitch = (Switch) convertView.findViewById(R.id.simpleSwitch);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
//            this.viewHolder = viewHolder;
        }

        viewHolder.txtTile.setText(dailyActivityItem.getTitle());
        viewHolder.txtHour.setText(dailyActivityItem.getHours());
        if(dailyActivityItem.getIsDone()==1){
            viewHolder.aSwitch.setChecked(true);
        }else{
            viewHolder.aSwitch.setChecked(false);
        }

        viewHolder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"selected:"+dailyActivityItem.getUid()+"-"+dailyActivityItem.getTitle(),Toast.LENGTH_LONG).show();
                GetDailyActivityAsync getDailyActivityAsync = new GetDailyActivityAsync();
                getDailyActivityAsync.execute(dailyActivityItem.getUid());
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    private class GetDailyActivityAsync extends AsyncTask<Long, Void, DailyActivities> {
        @Override
        protected DailyActivities doInBackground(Long... voids) {
            Log.d("Adapter",voids[0]+"");
            DailyActivities dailyActivities = db.dailyActivitiesDao().getDailyActivityById(voids[0]);
            boolean isCheck = viewHolder.aSwitch.isChecked();
            if(isCheck ==true){
                // Change into database
                dailyActivities.setIsDone(1);
                db.dailyActivitiesDao().update(dailyActivities);
            }else{
                // Change into database
                dailyActivities.setIsDone(0);
                db.dailyActivitiesDao().update(dailyActivities);
            }
            return dailyActivities;
        }

        @Override
        protected void onPostExecute(DailyActivities dailyActivities) {
            super.onPostExecute(dailyActivities);
        }
    }
}
