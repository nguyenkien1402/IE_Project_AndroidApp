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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.OtherUtilities;

import java.util.List;

/**
  Custom adapter for showing the activity model item
  This is the custom adapter for a list of activity
  Which is be used in the home screen
 **/
public class DailyActivityAdapter extends ArrayAdapter<DailyRoutineItem> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private List<DailyRoutineItem> datasets;
    Context context;
    TiamoDatabase db;
    ProgressDialog dialog ;
    boolean localCheck;

    public static class ViewHolder{
        TextView txtTile, txtHour;
        Switch aSwitch;
        ImageView imageView;
    }

    /*
    Construction of the adapter
    Necessary
     */
    public DailyActivityAdapter(List<DailyRoutineItem> datasets, Context context){
        super(context, R.layout.item_dailyactivity, datasets);
        this.datasets = datasets;
        this.context = context;
        db = SQLiteDatabase.getTiamoDatabase(context);
    }

    /*
    When user click the the item of the list
     */
    @Override
    public void onClick(View v) {
        String currentDate = DateUtilities.getCurrentDateInString().trim();
        String selectedDate = MainActivity.textToolbar.getText().toString().trim();
        if (currentDate.equals(selectedDate)) {
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
        }else{
            Toast.makeText(context,"Not Today",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    /*
    Populate the item list view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DailyRoutineItem dailyActivityItem = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            // Inflate the item of the list
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_dailyactivity, parent, false);
            viewHolder.txtTile = (TextView) convertView.findViewById(R.id.item_dailyactivity_title);
            viewHolder.txtHour = (TextView) convertView.findViewById(R.id.item_dailyactivity_hour);
//            viewHolder.aSwitch = (Switch) convertView.findViewById(R.id.simpleSwitch);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.daily_routine_icon);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Set the data for each item
        viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(OtherUtilities.getIcon(dailyActivityItem.getTitle())));
        viewHolder.txtTile.setText(dailyActivityItem.getTitle());
        viewHolder.txtHour.setText(dailyActivityItem.getHours());
        // Return the completed view to render on screen
        return convertView;
    }


    /*
     Handle the action when user click to the list of the item
     */
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
            DailyRoutine dailyRoutine = db.dailyActivitiesDao().getDailyActivityById(voids[0]);
            return null;
        }



        @Override
        protected void onPostExecute(Void dailyActivities) {
            super.onPostExecute(dailyActivities);
            dialog.dismiss();
        }
    }
}
