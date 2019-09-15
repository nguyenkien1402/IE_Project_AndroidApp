package com.mobile.tiamo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mobile.tiamo.R;
import com.mobile.tiamo.utilities.DayAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DashboardActivityAdapter extends ArrayAdapter<ActivityModelItem> {
    private int mResource;
    private Context context;
    Button showPopupBtn, closePopupBtn;
    PopupWindow popupWindow;
    LinearLayout linearLayout1;
    private LineChart chart;


    public DashboardActivityAdapter(Context context, int resource, List<ActivityModelItem> objects) {
        super(context, resource, objects);
        this.context = context;
        mResource = resource;
    }



    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ActivityModelItem model = new ActivityModelItem();
        model.setUid(getItem(position).getUid());
        model.setTitle(getItem(position).getTitle());
        model.setHours(getItem(position).getHours());
        model.setMinutes(getItem(position).getMinutes());

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(mResource, parent, false);

        final TextView tvTitle = (TextView) convertView.findViewById(R.id.dashboard_tvTitle);
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




        showPopupBtn = (Button) convertView.findViewById(R.id.showPopupBtn);
        linearLayout1 = (LinearLayout) convertView.findViewById(R.id.linearLayout1);
        showPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate the popup.xml layout file
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View customView = layoutInflater.inflate(R.layout.popup_graph,null);

                closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                TextView popUpTitle = (TextView) customView.findViewById(R.id.popup_dashboard_tvTitle);
                popUpTitle.setText(model.getTitle());

                TextView popUpTimeLeft = (TextView) customView.findViewById(R.id.popup_dashboard_tvTimeLeft);
                String strpopUpTimeLeft = "Time Left: " + model.getHours() + "h " + model.getMinutes() + "m";
                popUpTimeLeft.setText(strpopUpTimeLeft);

                TextView popUpTotalTime = (TextView) customView.findViewById(R.id.popup_dashboard_tvTotalTime);
                String strpopUpTotalTime = "Total Time: " + model.getHours() + "h " + model.getMinutes() + "m";
                popUpTotalTime.setText(strpopUpTotalTime);



                // Dismiss already existing popups
                if(popupWindow!=null)
                    popupWindow.dismiss();


                //instantiate popup window

                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                //display the popup window
                popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);
                popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
                popupWindow.setFocusable(false);
                popupWindow.setOutsideTouchable(true);


                //////////////////////////////////////////////////



                {   // // Chart Style // //
                    chart = customView.findViewById(R.id.chart1);

                    // background color
                    chart.setBackgroundColor(Color.WHITE);

                    // disable description text
                    chart.getDescription().setEnabled(false);


                    // set listeners
                    // chart.setOnChartValueSelectedListener(this);
                    chart.setDrawGridBackground(false);

                }

                XAxis xAxis;
                {   // // X-Axis Style // //
                    xAxis = chart.getXAxis();

                    String[] days =  getCurrentWeek();
                    ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart,days);

                    xAxis.setValueFormatter(xAxisFormatter);
                }

                {   // // Y-Axis Style // //

                    // disable dual axis (only use LEFT axis)
                    chart.getAxisRight().setEnabled(false);
                }



                setData(7, 180);

                // draw points over time
                chart.animateY(600);

                // get the legend (only possible after setting data)
                Legend l = chart.getLegend();

                // draw legend entries as lines
                l.setForm(Legend.LegendForm.LINE);
                ///////////////////////////////////////////////////






                //close the popup window on button click
                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

            }
        });
        return convertView;

    }

    public String[] getCurrentWeek(){
        // Get the current week dates
        DateFormat format = new SimpleDateFormat("EEE dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String[] days = new String[7];
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }


    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();


        for (int i = 0; i < count; i++) {

            float val  = (float) ThreadLocalRandom.current().nextInt(0, 2 + 1);

            values.add(new Entry(i, val, R.drawable.ic_star_black_24dp));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Time spent per day");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            set1.setFillColor(Color.BLACK);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }
}
