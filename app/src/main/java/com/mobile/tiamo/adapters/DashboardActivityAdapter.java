package com.mobile.tiamo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.mobile.tiamo.R;

import java.util.List;

public class DashboardActivityAdapter extends ArrayAdapter<ActivityModelItem> {
    private int mResource;
    private Context context;

    public DashboardActivityAdapter(Context context, int resource, List<ActivityModelItem> objects) {
        super(context, resource, objects);
        this.context = context;
        mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ActivityModelItem model = new ActivityModelItem();
        model.setUid(getItem(position).getUid());
        model.setTitle(getItem(position).getTitle());
        model.setHours(getItem(position).getHours());
        model.setMinutes(getItem(position).getMinutes());

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.dashboard_tvTitle);
        tvTitle.setText(model.getTitle());
        TextView tvTime = (TextView) convertView.findViewById(R.id.dashboard_tvTime);
        String strTimeSpent = "Time Left: " + model.getHours() + "h " + model.getMinutes() + "m";
        tvTime.setText(strTimeSpent);

        Integer selectedColor;
        Integer color1 = Color.parseColor("#d21e1e"); // Red
        Integer color2 = Color.parseColor("#e7e722"); // Yellow
        Integer color3 = Color.parseColor("#06d206"); // Green

        int progress = (int) ((float) model.getHours() / (position + 1) / model.getHours() * 100);

        if (progress < 25)
            selectedColor = color1;
        else if (progress >= 25 && progress < 75) {
            selectedColor = color2;
        } else
            selectedColor = color3;

        RoundCornerProgressBar progress1 = convertView.findViewById(R.id.dashboard_progress);
        progress1.setProgressColor(selectedColor);
        progress1.setProgressBackgroundColor(Color.parseColor("#808080"));

        progress1.setMax(model.getHours() * 100);
        progress1.setProgress(model.getHours() / (position + 1) * 100);


        return convertView;

    }
}
