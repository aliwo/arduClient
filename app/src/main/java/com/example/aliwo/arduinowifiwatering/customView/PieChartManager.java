package com.example.aliwo.arduinowifiwatering.customView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.aliwo.arduinowifiwatering.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
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

public class PieChartManager extends AppCompatActivity implements ChartSubject
{
    Context mcontext;

    public PieChartManager(Context context)
    {
        mcontext = context;
    }

    public void setPieChart(PieChart pieChart, float x, int subject)
    {

        //데이터 넣기
        List<PieEntry> entries = new ArrayList<>();
        switch(subject)
        {
            case 1:
                PieEntry entry = new PieEntry(x);
                entry.setLabel("white");
                entries.add(new PieEntry(x));
                break;

            case 2:
                entries.add(new PieEntry(x));
                break;

            case 3:
                entries.add(new PieEntry(x));
                break;
        }
        PieEntry rest = new PieEntry(100-x);
        entries.add(rest); // 보이지 않는 부분의 값.

        //색깔 넣기. 색깔을 resource로 부터 추출, 그래프에 입힘
        int white =  mcontext.getResources().getColor(R.color.white);
        int water = mcontext.getResources().getColor(R.color.water);
        int temperature = mcontext.getResources().getColor(R.color.temperature_chart_color);
        int green = mcontext.getResources().getColor(R.color.Green);
        int transparent = mcontext.getResources().getColor(R.color.transparent);
        int black = mcontext.getResources().getColor(R.color.black);
        int leapGreen = mcontext.getResources().getColor(R.color.leapGreen);


        PieDataSet set = new PieDataSet(entries, " ");
        set.setValueTextColor(transparent); // 파이차트 안에 들어있는 숫자를 안보이게 해요
        PieData data = new PieData(set);
        configureCommonPiechart(pieChart);
        pieChart.setHoleColor(transparent);
        pieChart.getLegend().setTextColor(black); // 왼쪽 아래 글자를 검은색으로 바꿔요

        switch(subject)
        {
            case 1:
                set.setLabel("습도"+x);
                set.setColors(leapGreen, white); // 파이 차트 색깔을 바꿔요
                break;

            case 2:
                set.setLabel("온도"+x);
                set.setColors(leapGreen, white);
                break;

            case 3:
                set.setLabel("온도"+x);
                set.setColors(green, white);
                break;
        }
        pieChart.setData(data);
        pieChart.invalidate();
    }

    public void  animatePieCharts(PieChart[] pieCharts)
    {
        for(int i=0; i<pieCharts.length; i++)
        {
            pieCharts[i].animateY(1000);
        }

    }

    private void configureCommonPiechart(PieChart pieChart)
    {
        Description desc = new Description();
        desc.setText(""); // description label 을 날려버립니다.
        pieChart.setDescription(desc);
        pieChart.getLegend().setTextSize(18f);
        pieChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        pieChart.getLegend().setYEntrySpace(3f);
    }

}
