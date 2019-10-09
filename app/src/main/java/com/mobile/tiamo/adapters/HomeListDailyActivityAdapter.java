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
import com.mobile.tiamo.utilities.OtherUtilities;

import java.util.List;

/*
  An adapter for the Home Fragment with the routine activity model
 */
public class HomeListDailyActivityAdapter extends ArrayAdapter<ActivityModelItem> implements View.OnClickListener {

    private List<ActivityModelItem> datasets;
    Context context;

    public static class ViewHolder{
        TextView txtTile, txtHour;
        ImageView img;
    }
    public HomeListDailyActivityAdapter(List<ActivityModelItem> datasets, Context context){
        super(context, R.layout.item_schedule_activity, datasets);
        this.datasets = datasets;
        this.context = context;
    }
    @Override
    public void onClick(View v) {

    }

    // Init view
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
            viewHolder.img = (ImageView) convertView.findViewById(R.id.daily_activity_icon);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTile.setText(activitiesModel.getTitle());
        viewHolder.img.setImageDrawable(context.getResources().getDrawable(OtherUtilities.getIcon(activitiesModel.getTitle())));

            if(activitiesModel.getMinutePractice() == 0 && activitiesModel.getHourPractice() != 0){
                String text = "You've done " + activitiesModel.getHourPractice() + " hours on this day";
                viewHolder.txtHour.setText(text);
            }else if(activitiesModel.getMinutePractice() != 0 && activitiesModel.getHourPractice() == 0) {
                String text = "You've done " + activitiesModel.getMinutePractice() + " minutes on this day";
                viewHolder.txtHour.setText(text);
            }else if(activitiesModel.getHourPractice() != 0 && activitiesModel.getMinutePractice() != 0){
                String text = "You've done " + activitiesModel.getHourPractice() +" hours and "+activitiesModel.getMinutePractice() + " minutes on this day";
                viewHolder.txtHour.setText(text);
            }else{
                viewHolder.txtHour.setText("");
            }



        return convertView;
    }
}
