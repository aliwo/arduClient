package com.example.aliwo.arduinowifiwatering.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by aliwo on 2017-03-26.
 */

public class WaterModule
{
    RestClient client;
    Context mContext;

    public WaterModule(RestClient client, Context context)
    {
        this.client = client;
        mContext = context;
    }

    public void water()
    {
        // 비동기 방식이기 때문에, water() 메소드가 리턴하는 값은 의미가 없음. 통신에 의한 결과는
        // 오직 onSuccess 에 의해서만 단독으로 처리해야 함.
        waterHttpHandler waterHandler = new waterHttpHandler(mContext);

        this.client.get("/water", null, waterHandler);
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
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
        Toast.makeText(mContext, "통신 실패", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString)
    {
        Log.d("WaterModule", "onSuccess 호출");
        if(responseString.contains("done"))
        {
            Toast.makeText(mContext, "물을 줬어요", Toast.LENGTH_LONG).show();
            Log.d("WaterModule", responseString);
        }
    }
}
