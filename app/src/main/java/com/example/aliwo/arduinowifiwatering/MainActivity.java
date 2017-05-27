package com.example.aliwo.arduinowifiwatering;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.aliwo.arduinowifiwatering.Sensor.ShakeDetector;
import com.example.aliwo.arduinowifiwatering.badgeManager.BadgeManager;
import com.example.aliwo.arduinowifiwatering.customView.PieChartManager;
import com.example.aliwo.arduinowifiwatering.http.RestClient;
import com.example.aliwo.arduinowifiwatering.http.HttpModule;
import com.github.mikephil.charting.charts.PieChart;

import me.leolin.shortcutbadger.ShortcutBadger;


public class MainActivity extends AppCompatActivity
{
    private Button button;
    private Button led_button;
    private Button set_up_button;
    private RestClient client;
    private HttpModule module;
    //private LinearLayout chartLayout;
    //private PieChart moistChart;
    //private PieChart temperatureChart;
    //private SensorManager sensorManager;
    //private PieChartManager chartManager;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private EditText input;
    private EditText dev_input;
    private LinearLayout layout;
    private AlertDialog.Builder bld;
    private ListView lvNavlist;
    private String[] navItems = {" ", "MK_1", "MK_2"};

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("setup URL");
        // Text뷰를 추가하장.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findMyFriends();
        initShakeDetector();
        module.getStatus(); // 그래프 그려버리기~
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause()
    {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    void findMyFriends() // findViewByid 혹은 클래스 멤버 초기화 등을 담당합니다.
    {
        button = (Button) findViewById(R.id.button);
        //moistChart = (PieChart) findViewById(R.id.moistchart);
        //temperatureChart = (PieChart) findViewById(R.id.temperatureChart);
        client = new RestClient(MainActivity.this);
        module = new HttpModule(client, MainActivity.this);
        //chartManager = new PieChartManager(MainActivity.this);
        //sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //chartLayout = (LinearLayout) findViewById(R.id.chartLayout);
        led_button = (Button) findViewById(R.id.LED_BUTTON);
        set_up_button = (Button) findViewById(R.id.set_up_button);
        lvNavlist = (ListView)findViewById(R.id.lv_activity_main_nav_list);

        lvNavlist.setAdapter(
                new ArrayAdapter<String>(this, R.layout.list_white_text, R.id.list_content, navItems));
        lvNavlist.setOnItemClickListener(new DrawerItemClickListener(this));

        setDialog(); // setup 다이얼로그를 초기화 합니다.
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                module.water();
            }
        });
        led_button.setOnClickListener(new View.OnClickListener() {
            boolean led_flag = false;
            Button self;
            @Override
            public void onClick(View v)
            {
                self = (Button)v;

                if(led_flag == false)
                {
                    module.ledOn();
                    self.setText("led = on");
                    led_flag = true;
                }
                else if(led_flag == true)
                {
                    module.ledOff();
                    led_flag = false;
                    self.setText("led = off");
                }
            }
        });
        set_up_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    //show()를 하기전에 레이아웃의 부모에서 레이아웃을 탈착
                    ViewGroup parentView = (ViewGroup) layout.getParent();
                    if(parentView != null)
                        parentView.removeAllViews();
                    bld.show(); // 다이얼 로그를 보여줍니다.
                } catch (IllegalStateException e)
                { e.printStackTrace(); }
            }
        });
    }

    void initShakeDetector()
    {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener()
        {

            @Override
            public void onShake(int count)
            {
				module.getStatus();
            }
        });
    }

    void setDialog()
    {
        input = new EditText(MainActivity.this);
        dev_input = new EditText(MainActivity.this);
        layout = new LinearLayout(MainActivity.this);
        bld = new AlertDialog.Builder(MainActivity.this);

        input.setText(RestClient.BASE_URL);
        dev_input.setHint("Are you a real developer?");
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);
        layout.addView(dev_input);

        bld.setTitle("Set Server URL");
        bld.setView(layout);
        bld.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if("devsw".compareTo(dev_input.getText().toString()) == 0)
                {
                    RestClient.BASE_URL = input.getText().toString();
                    Toast.makeText(MainActivity.this, "환영합니다. admin: SW", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "You shall not pass", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}



class DrawerItemClickListener implements ListView.OnItemClickListener
{
    Context mContext;
    public DrawerItemClickListener(Context context)
    {
        mContext = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position,
                            long id)
    {
        switch (position)
        {
            case 0:
                Toast.makeText(mContext,"0번 버튼", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(mContext,"1번 버튼", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(mContext,"2번 버튼", Toast.LENGTH_LONG).show();
                break;
            case 3:
                break;
            case 4:
                break;

        }
    }
}
