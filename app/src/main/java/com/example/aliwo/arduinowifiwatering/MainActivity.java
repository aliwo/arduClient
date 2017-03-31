package com.example.aliwo.arduinowifiwatering;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aliwo.arduinowifiwatering.http.RestClient;
import com.example.aliwo.arduinowifiwatering.http.WaterModule;

public class MainActivity extends AppCompatActivity
{
    Button button;
    RestClient client;
    WaterModule module;
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
            }
        });




    }



}
