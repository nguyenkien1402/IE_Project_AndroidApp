package com.mobile.tiamo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.tiamo.R;

import java.util.List;

public class DailyActivityAdapter extends ArrayAdapter<DailyActivityItem> implements View.OnClickListener {

    private List<DailyActivityItem> datasets;
    Context context;

    public static class ViewHolder{
        TextView txtTile, txtHour;
        Switch aSwitch;
    }

    public DailyActivityAdapter(List<DailyActivityItem> datasets, Context context){
        super(context, R.layout.item_dailyactivity, datasets);
        this.datasets = datasets;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DailyActivityItem dailyActivityItem = getItem(position);
        ViewHolder viewHolder;
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
        // Return the completed view to render on screen
        return convertView;
    }
}
