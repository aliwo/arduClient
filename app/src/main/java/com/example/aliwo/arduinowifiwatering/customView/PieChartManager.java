package com.example.aliwo.arduinowifiwatering.customView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.aliwo.arduinowifiwatering.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliwo on 2017-04-03.
 */

interface ChartSubject
{
    int waterChart = 1;
    int temperatureChart = 2;
    int waterLevelChart = 3;

}

public class PieChartManager extends AppCompatActivity
{
    Context mcontext;

    public PieChartManager(Context context)
    {
        mcontext = context;
    }

    public void setWaterPieChart(PieChart pieChart, float x, int subject)
    {

        //데이터 넣기
        List<PieEntry> entries = new ArrayList<>();
        switch(subject)
        {
            case 1:
                entries.add(new PieEntry(x, "습도 "+Float.toString(x)+"%"));
                break;

            case 2:
                entries.add(new PieEntry(x, "온도 "+Float.toString(x)+"도"));
                break;

            case 3:
                entries.add(new PieEntry(x, "수위 "+Float.toString(x)+"리터"));
                break;
        }
        entries.add(new PieEntry(100-x, " "));

        //색깔 넣기. 색깔을 resource로 부터 추출, 그래프에 입힘
        int white =  mcontext.getResources().getColor(R.color.white);
        int water = mcontext.getResources().getColor(R.color.water);
        int red = mcontext.getResources().getColor(R.color.RED);
        int green = mcontext.getResources().getColor(R.color.Green);
        PieDataSet set;
        switch(subject)
        {
            case 1:
                set = new PieDataSet(entries, "습도 정보");
                set.setColors(water, white);
                PieData data = new PieData(set);
                pieChart.setData(data);
                break;

            case 2:
                set = new PieDataSet(entries, "온도 정보");
                set.setColors(red, white);
                PieData data2 = new PieData(set);
                pieChart.setData(data2);
                break;

            case 3:
                set = new PieDataSet(entries, "수위");
                set.setColors(green, white);
                PieData data3 = new PieData(set);
                pieChart.setData(data3);
                break;
        }

        pieChart.invalidate();
    }

}
