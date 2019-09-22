package com.mobile.tiamo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.tiamo.R;

import java.util.List;

public class DashboardSleepingAdapter extends ArrayAdapter<DashboardSleepingItem> {

    private List<DashboardSleepingItem> list;
    private Context context;

    public static class ViewHolder{
        TextView txtDay, txtInBed, txtWakeup, txtAvg;
    }
    public DashboardSleepingAdapter(List<DashboardSleepingItem> list, Context context) {
        super(context,R.layout.item_sleeping_row,list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DashboardSleepingItem item = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_sleeping_row, parent, false);
            viewHolder.txtDay = (TextView) convertView.findViewById(R.id.dashboard_sleeping_item_day);
            viewHolder.txtInBed = (TextView) convertView.findViewById(R.id.dashboard_sleeping_item_at);
            viewHolder.txtWakeup = (TextView) convertView.findViewById(R.id.dashboard_sleeping_item_until);
            viewHolder.txtAvg = (TextView) convertView.findViewById(R.id.dashboard_sleeping_item_avg);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtDay.setText(item.getDay());
        viewHolder.txtInBed.setText(item.getInBed());
        viewHolder.txtWakeup.setText(item.getWakeUp());
        viewHolder.txtAvg.setText("Avg. "+item.getAvg());

        return convertView;
    }
}
