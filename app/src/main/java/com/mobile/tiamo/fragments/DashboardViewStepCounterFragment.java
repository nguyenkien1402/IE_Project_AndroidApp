package com.mobile.tiamo.fragments;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Update;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.StepsTakenModel;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.services.StepDetector;
import com.mobile.tiamo.services.StepListener;
import com.mobile.tiamo.services.StepsCounterService;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 A StepTaken Dashboard
 **/
public class DashboardViewStepCounterFragment extends Fragment implements SensorEventListener, StepListener {

    private View view;
    private AnyChartView chart;
    private View popupView;
    private ImageView imHappy, imSad, imNeutral, imMood;
    private Button btnSetMood;
    private TextView tvToday, tvStepRunningToday, tvStepTakenToday;
    public int stepsTaken = 0;
    private List<StepsTakenModel> stepsTakenModelsLastWeek;
    private List<StepsTakenModel> stepsTakenModelsThisWeek;
    private List<StepsTakenModel> stepsTakenModels;
    private TiamoDatabase db;
    private String[] days = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    private int stepsToday = 0;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private LinearLayout btnSetMood2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_step_counter, container, false);
        db = SQLiteDatabase.getTiamoDatabase(getActivity());
        stepsTakenModelsLastWeek = new ArrayList<StepsTakenModel>();
        stepsTakenModelsThisWeek = new ArrayList<StepsTakenModel>();
        stepsTakenModels = new ArrayList<StepsTakenModel>();
        initComponent();
        btnSetMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupMoodView();
            }
        });
        btnSetMood2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupMoodView();
            }
        });
        gettingStepsTaken();
        // Get the data from server
        LoadingChartAysnc loadingChartAysnc = new LoadingChartAysnc();
        loadingChartAysnc.execute();

        Intent service = new Intent(getActivity(), StepsCounterService.class);
        getActivity().startService(service);

        return view;
    }

    private void gettingStepsTaken(){
        stepsTaken = SavingDataSharePreference.getDataInt(getActivity(), Messages.LOCAL_DATA_STEP, DateUtilities.getCurrentDateInString());
        if(stepsTaken == -1){
            tvStepTakenToday.setText(0+"");
        }else{
            tvStepTakenToday.setText(stepsTaken+"");
        }

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("StepCounter","Pause");
        sensorManager.unregisterListener(this);
    }

    /**
         Init the main component of the dashboard
         */
    private void initComponent(){
        chart = view.findViewById(R.id.chart1);
        imMood = view.findViewById(R.id.mood_view);
        btnSetMood = view.findViewById(R.id.btnSetMood);
        tvToday = view.findViewById(R.id.step_today);
        tvStepTakenToday = view.findViewById(R.id.step_takens_today);
        tvStepRunningToday = view.findViewById(R.id.step_running_today);
        btnSetMood2 = view.findViewById(R.id.btnSetMood2);
        tvToday.setText(DateUtilities.getCurrentDateInString());
        tvStepRunningToday.setText("0 km");
    }

    private void getData(int day){
        /**
          This function is used to manipulate the data before adding to the list
          Get the database on the current day of the week
          And the data of the last week.
          Parameter:
            day: day before this day
          return:
            None
         **/
        try {
            String today = DateUtilities.getCurrentDateInString();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR,-day);
            Date datebefore = calendar.getTime();
            String dateBeforeStr = dateFormat.format(datebefore);

            stepsTakenModels = db.stepsTakenDao().getStepsTakenInrange(dateBeforeStr,today);
//            stepsTakenModels.addAll(db.stepsTakenDao().getStepsTakenInrange("01-10-2019",today));

            for(int i = 0 ; i <  7 ; i++){
                stepsTakenModelsLastWeek.add(stepsTakenModels.get(i));
            }

            for(int i = 7 ; i<stepsTakenModels.size() ; i++){
                stepsTakenModelsThisWeek.add(stepsTakenModels.get(i));
            }
//            if(stepsTakenModelsThisWeek.size()==0){
//                StepsTakenModel stepsTakenModel = new StepsTakenModel();
//                stepsTakenModel.setSteps(659);
//                stepsTakenModel.setDate(today);
//                stepsTakenModelsThisWeek.add(stepsTakenModel);
//            }

        }catch (Exception e){
            Log.d("TAG",e.getMessage());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {
        stepsTaken++;
        tvStepTakenToday.setText(stepsTaken+"");
        Log.d("SleepingFragment",stepsTaken+"");
        if(stepsTaken % 20 ==0){
            if(SavingDataSharePreference.getDataInt(getActivity(),Messages.LOCAL_DATA_STEP, DateUtilities.getCurrentDateInString()) != -1){
                int current = SavingDataSharePreference.getDataInt(getActivity(),Messages.LOCAL_DATA_STEP, DateUtilities.getCurrentDateInString());
                current = current + stepsTaken;
                SavingDataSharePreference.savingLocalData(getActivity(), Messages.LOCAL_DATA_STEP, DateUtilities.getCurrentDateInString(),current);
            }
        }
    }



    /*
      AsyncTask class to get the data
     */
    private class LoadingChartAysnc extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            String currentDayinAbb = DateUtilities.getCurrentDayInAbb();
            switch (currentDayinAbb){
                case "Mon":
                    getData(7);
                    break;
                case "Tue":
                    getData(8);
                    break;
                case "Wed":
                    getData(9);
                    break;
                case "Thu":
                    getData(10);
                    break;
                case "Fri":
                    getData(11);
                    break;
                case "Sat":
                    getData(12);
                    break;
                case "Sun":
                    getData(13);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(stepsTakenModelsThisWeek.size() != 0 && stepsTakenModelsLastWeek.size() != 0){
                initChart();
            }
            return;
        }
    }


    // Get the mood from user
    private void initPopupMoodView(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(false);

        LayoutInflater layoutInflaterInput = LayoutInflater.from(getActivity());
        popupView = layoutInflaterInput.inflate(R.layout.popup_mood, null);
        imHappy = popupView.findViewById(R.id.mood_happy);
        imNeutral = popupView.findViewById(R.id.mood_neutral);
        imSad = popupView.findViewById(R.id.mood_sad);

        alertDialogBuilder.setView(popupView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        imHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set
                imMood.setImageResource(R.drawable.happy_64);
                alertDialog.dismiss();
            }
        });

        imNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set
                imMood.setImageResource(R.drawable.neutral_64);
                alertDialog.dismiss();
            }
        });

        imSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set
                imMood.setImageResource(R.drawable.sad_64);
                alertDialog.dismiss();
            }
        });
    }

    // Init the chart to compare steps taken this week and last week
    private void initChart() {
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Steps taken in this week and last week");

        cartesian.yAxis(0).title("Steps");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        Log.d("Size This Week",stepsTakenModelsThisWeek.size()+"");
        for(int i = 0 ; i<7 ; i++){
            CustomDataEntry c = new CustomDataEntry(days[i],stepsTakenModelsLastWeek.get(i).getSteps());
            if(i<stepsTakenModelsThisWeek.size()){
                c.setValue("value2",stepsTakenModelsThisWeek.get(i).getSteps());
            }else{
                if(DateUtilities.getDayInAbbBySelectedDate(stepsTakenModelsLastWeek.get(i).getDate()).equals(
                        DateUtilities.getCurrentDayInAbb()))
                {
                    c.setValue("value2",stepsToday);
                }else{
                    c.setValue("value2",0);
                }
            }
            seriesData.add(c);
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Last Week");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("This Week");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        chart.setChart(cartesian);
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value){
            super(x,value);
            setValue("value",value);
        }
        CustomDataEntry(String x, Number value, Number value2){
            super(x,value);
            setValue("value",value);
            setValue("value2",value2);
        }
        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }

}
