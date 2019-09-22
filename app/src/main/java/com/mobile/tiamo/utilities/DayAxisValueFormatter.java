package com.mobile.tiamo.utilities;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter extends ValueFormatter
{
    public DayAxisValueFormatter(BarLineChartBase<?> chart, String[] days) {
        this.chart = chart;
        this.currentDays = days;
    }

    private final BarLineChartBase<?> chart;
    private final String[] currentDays;

    @Override
    public String getFormattedValue(float value) {

        int days = (int) value;
        return currentDays[days] ;
        }

}

