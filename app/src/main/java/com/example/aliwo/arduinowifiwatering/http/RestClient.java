package com.example.aliwo.arduinowifiwatering.http;

/**
 * Created by Administrator on 2017-01-03.
 */

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * Created by Youngdo on 2016-02-20.
 */
// java 클래스를 모아놓은 파일에 통신을 위한 패키지(간단히 말하면 폴더) 를 하나 만들어서 이 RestClient 파일을 저장하는 것을 추천합니다.
// 그리고 build.gradle (Module:app) 에서 다음과 같이 선언하세요
// compile 'com.loopj.android:android-async-http:1.4.9'
// 마지막으로 안드로이드 매니페스트에 <uses-permission android:name="android.permission.INTERNET"/> 를 선언하는 걸 잊지 마세요.

public class RestClient
{
    private static Context context;
    public static String BASE_URL = "http://google.com"; //서버 주소와 포트를 입력하세요
    private static AsyncHttpClient client = new AsyncHttpClient();

    public RestClient(Context context)
    {
        if(this.context == null)
        {
            this.context = context;
        }
    }
    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        client.get(getAbsoluteUrl(url), params, responseHandler);
        PersistentCookieStore CookieStore = new PersistentCookieStore(context);
        client.setCookieStore(CookieStore);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
    {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public CookieStore getCookie()
    {
        HttpContext httpContext = client.getHttpContext();
        CookieStore cookieStore = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);
        return cookieStore;
    }
}

