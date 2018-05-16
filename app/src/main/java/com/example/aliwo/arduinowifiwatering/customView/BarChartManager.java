package com.example.aliwo.arduinowifiwatering.customView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.aliwo.arduinowifiwatering.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliwo on 2017-06-02.
 */

public class BarChartManager extends AppCompatActivity
{
    Context mcontext;
    int leapGreen;
    int black;
    int white;
    int water;
    int red;

    public BarChartManager(Context mcontext)
    {
        this.mcontext = mcontext;

        this.white =  mcontext.getResources().getColor(R.color.white);
        this.water = mcontext.getResources().getColor(R.color.water);
        int temperature = mcontext.getResources().getColor(R.color.temperature_chart_color);
        int green = mcontext.getResources().getColor(R.color.Green);
        int transparent = mcontext.getResources().getColor(R.color.transparent);
        this.black = mcontext.getResources().getColor(R.color.black);
        this.leapGreen = mcontext.getResources().getColor(R.color.leapGreen);
        this.red = mcontext.getResources().getColor(R.color.RED);
    }

    public void setBarChart(BarChart barChart, float x)
    {
        List<BarEntry> entries = new ArrayList<>();
        BarEntry entry = new BarEntry(1f, x); // x 값을 똑같이 넣으면 stacked bar가 됩니다.
        entries.add(entry);
        if(x >= 15)
        {
            BarEntry sub_entry = new BarEntry(1f, 15f);
            entries.add(sub_entry);
        }
        if(x >= 10)
        {
            BarEntry sub_entry = new BarEntry(1f, 5f);
            entries.add(sub_entry);
        }


        BarDataSet set = new BarDataSet(entries, " ");
        set.setColors(red,leapGreen,water);

        BarData data = new BarData(set);
        data.setBarWidth(0.3f);

        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setAxisMinValue(0);
        leftYAxis.setAxisMaxValue(30);
        leftYAxis.setLabelCount(1);

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setAxisMinValue(0);
        rightYAxis.setAxisMaxValue(30);
        rightYAxis.setLabelCount(1);


        barChart.setData(data);
        barChart.invalidate();


    }
}
