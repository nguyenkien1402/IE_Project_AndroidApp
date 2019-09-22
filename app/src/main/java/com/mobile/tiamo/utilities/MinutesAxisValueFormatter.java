package com.mobile.tiamo.utilities;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MinutesAxisValueFormatter extends ValueFormatter {

    private final BarLineChartBase<?> chart;

    public MinutesAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {
        return String.format("%d", (int) value);
    }

}
