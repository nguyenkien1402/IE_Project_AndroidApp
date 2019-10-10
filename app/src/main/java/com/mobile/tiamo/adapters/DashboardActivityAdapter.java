package com.mobile.tiamo.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mobile.tiamo.utilities.MinutesAxisValueFormatter;
import com.mobile.tiamo.utilities.OtherUtilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
    An adapter for the list of the dashboard
    Showing the dashboard in the fragment.
 **/
public class DashboardActivityAdapter extends ArrayAdapter<ActivityModelItem> {
    private int mResource;
    private Context context;
    private Button showPopupBtn, closePopupBtn;
    private LinearLayout linearLayout1;
    private LineChart chart;
    private List<DailyActivityHobbyModelItem> activityHobbyModelItems;
    private ImageView activityImgView;

    public DashboardActivityAdapter(Context context, int resource, List<ActivityModelItem> objects, List<DailyActivityHobbyModelItem> activityHobbyModelItems) {
        super(context, resource, objects);
        this.context = context;
        mResource = resource;
        this.activityHobbyModelItems = activityHobbyModelItems;
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
        model.setMinutePractice(getItem(position).getMinutePractice());
        model.setHourPractice(getItem(position).getHourPractice());

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(mResource, parent, false);

        RoundCornerProgressBar progress1 = convertView.findViewById(R.id.dashboard_progress);

        int totalMinutesTarget = model.getHours() * 60 + model.getMinutes();
        int totalMinutesPracticed = model.getHourPractice() * 60 + model.getMinutePractice();
        int totalMinutesLeft = totalMinutesTarget - totalMinutesPracticed;
        if (totalMinutesLeft < 0)
            model.setTotalMinutesLeft(0);
        else
            model.setTotalMinutesLeft(totalMinutesLeft);

        progress1.setMax(totalMinutesTarget);
        progress1.setProgress(totalMinutesPracticed);

        // Progress bar color
        int progress = (totalMinutesPracticed / totalMinutesTarget) * 100;
        int color1 = Color.parseColor("#e7e722"); // Yellow
        int color2 = Color.parseColor("#06d206"); // Green
        int selectedColor = (progress < 100) ? color1 : color2;
        progress1.setProgressColor(selectedColor);
        progress1.setProgressBackgroundColor(Color.parseColor("#808080"));

        //Set values for text views
        final TextView tvTitle = (TextView) convertView.findViewById(R.id.dashboard_tvTitle);
        tvTitle.setText(model.getTitle());
        TextView tvTime = (TextView) convertView.findViewById(R.id.dashboard_tvTime);
        String strTimeSpent = String.format("Time Left: %dh %dm", model.getTotalMinutesLeft() / 60, model.getTotalMinutesLeft() % 60);
        tvTime.setText(strTimeSpent);

        //Get icons for activities
        activityImgView = (ImageView) convertView.findViewById(R.id.dashboard_activity_icon);
        activityImgView.setImageDrawable(ContextCompat.getDrawable(context, OtherUtilities.getIcon(model.getTitle())));

        showPopupBtn = (Button) convertView.findViewById(R.id.showPopupBtn);
        linearLayout1 = (LinearLayout) convertView.findViewById(R.id.linearLayout1);
        showPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate the popup.xml layout file
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setCancelable(false);

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View customView = layoutInflater.inflate(R.layout.popup_graph,null);
                alertDialogBuilder.setView(customView);
                // Inflate the popup dialog from a layout xml file.

                closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                closePopupBtn.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.red_background), PorterDuff.Mode.MULTIPLY);

                TextView popUpTitle = (TextView) customView.findViewById(R.id.popup_dashboard_tvTitle);
                popUpTitle.setText(model.getTitle());

                TextView popUpTimeLeft = (TextView) customView.findViewById(R.id.popup_dashboard_tvTimeLeft);
                String strPopUpTimeLeft = String.format("Time Left: %dh %dm", model.getTotalMinutesLeft() / 60, model.getTotalMinutesLeft() % 60);
                popUpTimeLeft.setText(strPopUpTimeLeft);

                TextView popUpTotalTime = (TextView) customView.findViewById(R.id.popup_dashboard_tvTotalTime);
                String strPopUpTotalTime = String.format("Total Time: %dh %dm", model.getHours(), model.getMinutes());
                popUpTotalTime.setText(strPopUpTotalTime);

                // Init popup dialog view and it's ui controls.

                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();


                //////////////////////////////////////////////////

                {   // // Chart Style // //
                    chart = customView.findViewById(R.id.chart1);

                    // background color
                    chart.setBackgroundColor(Color.WHITE);

                    // disable description text
                    chart.getDescription().setEnabled(false);

                    chart.setTouchEnabled(false);


                    // set listeners
                    // chart.setOnChartValueSelectedListener(this);
                    chart.setDrawGridBackground(false);

                }

                XAxis xAxis;
                {   // // X-Axis Style // //
                    xAxis = chart.getXAxis();

                    String[] days = getCurrentWeek("EEE dd");
                    ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart,days);

                    xAxis.setValueFormatter(xAxisFormatter);
                }

                {   // // Y-Axis Style // //

                    // disable dual axis (only use LEFT axis)

                    chart.getAxisRight().setEnabled(false);
                }

                setData(model.getUid());

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
                        alertDialog.cancel();
                    }
                });

            }
        });
        return convertView;

    }

    private String[] getCurrentWeek(String dateFormat) {
        // Get the current week dates
        DateFormat format = new SimpleDateFormat(dateFormat);
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


    private void setData(long uid) {

        ArrayList<Entry> values = new ArrayList<>();
        String[] currentWeek = getCurrentWeek("dd-MM-yyyy");

        for (int i = 0; i < 7; i++) {
            float mins = 0;
            for (DailyActivityHobbyModelItem item : activityHobbyModelItems) {
                if (item.getUid() == uid && item.getDateCreated().equals(currentWeek[i])) {
                    mins = item.getMinutes() + item.getHours() * 60;
                }
            }

            values.add(new Entry(i, mins, R.drawable.ic_star_black_24dp));
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
            set1 = new LineDataSet(values, "Minutes spent per day");

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
            // set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            set1.setFillColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            ValueFormatter yAxisFormatter = new MinutesAxisValueFormatter(chart);
            data.setValueFormatter(yAxisFormatter);

            // set data
            chart.setData(data);
        }
    }
}
