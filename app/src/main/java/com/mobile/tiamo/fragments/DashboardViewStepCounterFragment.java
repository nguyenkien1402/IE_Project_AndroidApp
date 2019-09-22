package com.mobile.tiamo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.adapters.HomeListDailyActivityAdapter;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewStepCounterFragment extends Fragment {

    private View view;
    private AnyChartView chart;
    private String[] days = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_step_counter, container, false);
        chart = view.findViewById(R.id.chart1);

        initChart();

        return view;
    }

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
        seriesData.add(new CustomDataEntry("Mon", 1200,600));
        seriesData.add(new CustomDataEntry("Tue", 2400, 2000));
        seriesData.add(new CustomDataEntry("Wed", 1080, 500));
        seriesData.add(new CustomDataEntry("Thu", 4200, 1200));
        seriesData.add(new CustomDataEntry("Fri", 5601, 1500));
        seriesData.add(new CustomDataEntry("Sat", 800, 3800));
        seriesData.add(new CustomDataEntry("Sun", 1291, 5600));

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("This week");
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
        series2.name("Last Week");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
//
//        Line series3 = cartesian.line(series3Mapping);
//        series3.name("Tequila");
//        series3.hovered().markers().enabled(true);
//        series3.hovered().markers()
//                .type(MarkerType.CIRCLE)
//                .size(4d);
//        series3.tooltip()
//                .position("right")
//                .anchor(Anchor.LEFT_CENTER)
//                .offsetX(5d)
//                .offsetY(5d);

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
