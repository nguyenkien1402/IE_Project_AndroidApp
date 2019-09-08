package com.mobile.tiamo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.tiamo.R;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.fragments.ScheduleActivityPagerFragment;

import java.util.List;

public class HomeListDailyActivityAdapter extends ArrayAdapter<ActivityModelItem> implements View.OnClickListener {

    private List<ActivityModelItem> datasets;
    Context context;

    public static class ViewHolder{
        TextView txtTile, txtHour;
    }
    public HomeListDailyActivityAdapter(List<ActivityModelItem> datasets, Context context){
        super(context, R.layout.item_schedule_activity, datasets);
        this.datasets = datasets;
        this.context = context;
    }
    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ActivityModelItem activitiesModel = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_schedule_activity, parent, false);
            viewHolder.txtTile = (TextView) convertView.findViewById(R.id.item_schedule_activity_title);
            viewHolder.txtHour = (TextView) convertView.findViewById(R.id.item_schedule_activity_hour);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTile.setText(activitiesModel.getTitle());
        if(activitiesModel.getMinutes() == 0) {
            String text = activitiesModel.getHours() + " hours per week";
            viewHolder.txtHour.setText(text);
        }else{
            String text = activitiesModel.getHours() +" hours, "+activitiesModel.getMinutes()+" minutes per week";
            viewHolder.txtHour.setText(text);
        }


        // Return the completed view to render on screen
        return convertView;
    }
}
