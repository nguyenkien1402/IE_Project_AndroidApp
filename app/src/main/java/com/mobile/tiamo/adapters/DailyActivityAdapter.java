package com.mobile.tiamo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
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

public class DailyActivityAdapter extends ArrayAdapter<DailyActivityItem> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private List<DailyActivityItem> datasets;
    Context context;
    TiamoDatabase db;
//    DailyActivityItem dailyActivityItem;
    ViewHolder viewHolder;
    ProgressDialog dialog ;
    boolean localCheck;
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
        int position = (Integer)v.getTag();
        int isCheck = getItem(position).getIsDone();
        if(isCheck == 1){
            // Change to No
            localCheck = false;
            GetDailyActivityAsync getDailyActivityAsync = new GetDailyActivityAsync();
            getDailyActivityAsync.execute(getItem(position).getUid());
            Log.d("Adapter",position+"-Y:"+getItem(position).getTitle() + ":"+getItem(position).getIsDone());
        }else{
            // Change to 1
            localCheck = true;
            GetDailyActivityAsync getDailyActivityAsync = new GetDailyActivityAsync();
            getDailyActivityAsync.execute(getItem(position).getUid());
            Log.d("Adapter",position+"-N"+getItem(position).getTitle() + ":"+getItem(position).getIsDone());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        int position = (Integer)buttonView.getTag();
//        DailyActivityItem dailyActivityItem = getItem(position);
//        Log.d("Adapter","is check:"+isChecked);
//        localCheck = isChecked;
//        Toast.makeText(context,"selected:"+dailyActivityItem.getUid()+"-"+dailyActivityItem.getTitle(),Toast.LENGTH_LONG).show();
//        GetDailyActivityAsync getDailyActivityAsync = new GetDailyActivityAsync();
//        getDailyActivityAsync.execute(dailyActivityItem.getUid());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DailyActivityItem dailyActivityItem = getItem(position);
//        final ViewHolder viewHolder;
//        this.dailyActivityItem = dailyActivityItem;

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
        }

        viewHolder.txtTile.setText(dailyActivityItem.getTitle());
        viewHolder.txtHour.setText(dailyActivityItem.getHours());
        if(dailyActivityItem.getIsDone()==1){
            viewHolder.aSwitch.setChecked(true);
        }else{
            viewHolder.aSwitch.setChecked(false);
        }
        viewHolder.aSwitch.setTag(position);
        viewHolder.aSwitch.setOnClickListener(this);
//        viewHolder.aSwitch.setOnCheckedChangeListener(this);
//        viewHolder.aSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isCheck = viewHolder.aSwitch.isChecked();
//                Log.d("Adapter",dailyActivityItem.getTitle()+"-"+isCheck+"");
//            }
//        });
        // Return the completed view to render on screen
        return convertView;
    }

    private class GetDailyActivityAsync extends AsyncTask<Long, Void, Void> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Updating");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Long... voids) {
            Log.d("Adapter",voids[0]+"");
            if(localCheck ==true){
                // Change into database
                Log.d("Adapter","Change to 1");
                db.dailyActivitiesDao().updateIsDone(voids[0],1);
            }
            if(localCheck == false){
                // Change into database
                Log.d("Adapter","Change to zero");
                db.dailyActivitiesDao().updateIsDone(voids[0],0);
            }
            DailyActivities dailyActivities = db.dailyActivitiesDao().getDailyActivityById(voids[0]);
            Log.d("Adapter",dailyActivities.getTitle() + "-"+dailyActivities.getIsDone());
            return null;
        }



        @Override
        protected void onPostExecute(Void dailyActivities) {
            super.onPostExecute(dailyActivities);
            dialog.dismiss();
        }
    }
}
