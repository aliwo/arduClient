package com.example.aliwo.arduinowifiwatering;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.aliwo.arduinowifiwatering.http.RestClient;
import com.example.aliwo.arduinowifiwatering.http.WaterModule;
import com.github.mikephil.charting.charts.PieChart;
import com.example.aliwo.arduinowifiwatering.customView.PieChartManager;



public class MainActivity extends AppCompatActivity
{
    Button button;
    RestClient client;
    WaterModule module;
    RelativeLayout chartLayout;
    PieChart moistChart;
    PieChart temperatureChart;
    PieChart waterLevelChart;
    int result;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new RestClient(MainActivity.this);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                module = new WaterModule(client, MainActivity.this);
                module.water();
                moistChart.animateY(1000);
                temperatureChart.animateY(1000);
                waterLevelChart.animateY(1000);
            }
        });
        chartLayout = (RelativeLayout) findViewById(R.id.chartLayout);
        moistChart = (PieChart) findViewById(R.id.moistchart);
        temperatureChart = (PieChart) findViewById(R.id.temperatureChart);
        waterLevelChart = (PieChart) findViewById(R.id.waterLevelChart);


        PieChartManager chartmanager = new PieChartManager(MainActivity.this);
        chartmanager.setWaterPieChart(moistChart, 82.5f, 1); // 습도 표시 차트 생성
        chartmanager.setWaterPieChart(temperatureChart, 24.5f, 2); // 온도 표시 차트 생성
        chartmanager.setWaterPieChart(waterLevelChart, 70.5f, 3); // 수위 표시 차트 생성

    }



}
