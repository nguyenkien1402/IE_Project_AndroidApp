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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<ScheduleModel> implements View.OnClickListener {

    private List<ScheduleModel> datasets;
    Context context;

    public static class ViewHolder{
        TextView txtTile, txtHour, txtDay;
    }

    public ScheduleAdapter(List<ScheduleModel> datasets, Context context){
        super(context, R.layout.item_schedule, datasets);
        this.datasets = datasets;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ScheduleModel scheduleModel = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_schedule, parent, false);
            viewHolder.txtTile = (TextView) convertView.findViewById(R.id.item_schedule_title);
            viewHolder.txtDay = (TextView) convertView.findViewById(R.id.item_schedule_day);
            viewHolder.txtHour = (TextView) convertView.findViewById(R.id.item_schedule_hour);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTile.setText(scheduleModel.getTitle());
        viewHolder.txtDay.setText(scheduleModel.getDays());
        viewHolder.txtHour.setText(scheduleModel.getHours());
        // Return the completed view to render on screen
        return convertView;
    }
}
