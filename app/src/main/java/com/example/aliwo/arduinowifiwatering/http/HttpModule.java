package com.example.aliwo.arduinowifiwatering.http;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.aliwo.arduinowifiwatering.MainActivity;
import com.example.aliwo.arduinowifiwatering.R;
import com.example.aliwo.arduinowifiwatering.customView.BarChartManager;
import com.example.aliwo.arduinowifiwatering.customView.PieChartManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by aliwo on 2017-03-26.
 */

public class HttpModule
{
    private RestClient client;
    private Context mContext;
    private StatusHttpHandler statusHandler;
    private waterHttpHandler waterHandler;
    private LedHttpHandler ledHttpHandler;
    private FlushHttpHandler flushHttpHandler;
    private GoogleHandler googleHandler;
    private static String arduino_id = "00000000";
    private RequestParams params;

    public HttpModule(RestClient client, Context context)
    {
        this.client = client;
        mContext = context;
        waterHandler = new waterHttpHandler(context);
        statusHandler = new StatusHttpHandler(context);
        ledHttpHandler = new LedHttpHandler(context);
        flushHttpHandler = new FlushHttpHandler(context);
        googleHandler = new GoogleHandler();
    }

    public void water()
    {
        params = new RequestParams();
        params.add("instruction", "water");
        this.client.post("/mobile_order/"+arduino_id, params, waterHandler);
        // 비동기 방식이기 때문에, water() 메소드가 리턴하는 값은 의미가 없음. 통신에 의한 결과는
        // 오직 onSuccess 에 의해서만 단독으로 처리해야 함.
    }
    public void getStatus()
    {
        params = new RequestParams();
        params.add("instruction", "status");
        this.client.post("/mobile_order/"+arduino_id, params, statusHandler);
    }
    public void ledOn()
    {
        params = new RequestParams();
        params.add("instruction", "led_on");
        this.client.post("/mobile_order/"+arduino_id, params, ledHttpHandler);
    }
    public  void ledOff()
    {
        params = new RequestParams();
        params.add("instruction", "led_off");
        this.client.post("/mobile_order/"+arduino_id, params, ledHttpHandler);
    }
    public  void flush()
    {
        params = new RequestParams();
        params.add("instruction", "flush");
        this.client.post("/mobile_order/"+arduino_id, params, flushHttpHandler);
    }

    public void google()
    {
        this.client.get("/", null, googleHandler);
    }
}

class waterHttpHandler extends TextHttpResponseHandler
{
    Context mContext;
    public waterHttpHandler(Context context)
    {
        mContext = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString)
    {
        Log.d("HttpModule", "onSuccess 호출");
        Log.d("WatterHttpHandler.", responseString);
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
        Toast.makeText(mContext, "통신 실패", Toast.LENGTH_LONG).show();
    }
}

class StatusHttpHandler extends JsonHttpResponseHandler
{
    Context mContext;
    private int temperature;
    private int humidity;
    PieChart moistChart;
    BarChart temperatureChart;

    public StatusHttpHandler(Context context)
    {
        mContext = context;
        moistChart = (PieChart) ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.moistchart);
        temperatureChart = (BarChart) ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.temperatureChart);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
    {
        super.onSuccess(statusCode, headers, response);
        Log.d("StatusHandler", "onSuccess 호출");
        try
        {
            temperature = response.getInt("temperature");
            humidity = response.getInt("humidity");

            final PieChartManager chartManager = new PieChartManager(mContext);
            final BarChartManager barChartManager = new BarChartManager(mContext);
            barChartManager.setBarChart(temperatureChart, (float) temperature); // 온도 표시 차트 생성
            chartManager.setPieChart(moistChart, (float) humidity, PieChartManager.waterChart); // 습도 표시 차트 생성
            chartManager.animatePieCharts(new PieChart[]{moistChart});

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response)
    {
        super.onSuccess(statusCode, headers, response);
        Log.d("StatusHandler", "onSuccess 호출");

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString)
    {
        super.onSuccess(statusCode, headers, responseString);
        Log.d("StatusHandler", "onSuccess 호출");

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
    {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Toast.makeText(mContext, "서버 점검중입니다.", Toast.LENGTH_LONG).show();

        final PieChartManager chartManager = new PieChartManager(mContext);
        //chartManager.setPieChart(temperatureChart, (float) 28, PieChartManager.temperatureChart); // 실패시 기본값
        chartManager.setPieChart(moistChart, (float) 45, PieChartManager.waterChart); // 통신 실패시 기본값 설정
        chartManager.animatePieCharts(new PieChart[]{moistChart});

    }

    public int getTemperature()
    {
        return this.temperature;
    }

    public int getHumidity()
    {
        return this.humidity;
    }

}

class LedHttpHandler extends TextHttpResponseHandler
{
    Context mContext;
    Button led_button;

    public LedHttpHandler(Context context)
    {
        this.mContext = context;
        led_button = (Button) ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.led);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString)
    {
        Log.d("led", "led 통신 전송");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
        Log.d("led", "led 통신 실패");
        if(led_button != null) led_button.setText("led 사용불가");
    }

}

class FlushHttpHandler extends TextHttpResponseHandler
{
    Context mContext;

    public FlushHttpHandler(Context context)
    {
        mContext = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString)
    {
        Log.d("flush", "flush 통신 전송");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
        Log.d("flush", "flush 통신 실패");
    }
}

class GoogleHandler extends TextHttpResponseHandler
{
    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString)
    {
        Log.d("GoogleHandler", responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {

    }
}