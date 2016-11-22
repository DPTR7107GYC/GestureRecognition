package com.example.actionturnwrist;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;

import android.view.WindowManager;
import android.widget.LinearLayout;


public class SongListActivity extends Activity implements SensorEventListener {



    private SensorManager sensorManager = null;
    private Sensor gyroscopeSensor = null;
    private Sensor linearAccelerationSensor = null;
    private int flag = 0;
    private long curtime;
    private long curtime1;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;
    private int num = 1;
    private int numed=1;
    private boolean flag1 = false;
    private SoundPool soundPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);

        linearLayout1 = (LinearLayout)findViewById(R.id.song1) ;
        linearLayout2 = (LinearLayout)findViewById(R.id.song2) ;
        linearLayout3 = (LinearLayout)findViewById(R.id.song3) ;
        linearLayout4 = (LinearLayout)findViewById(R.id.song4) ;
//        linearLayout5 = (LinearLayout)findViewById(R.id.song5) ;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        soundPool= new SoundPool(1, AudioManager.STREAM_RING,5);
        soundPool.load(this,R.raw.beep,1);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_FASTEST);
        curtime1=0;
        curtime=0;
        initView(num);
    }

    //
    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_GYROSCOPE:
                if (300 > event.values[0] && event.values[0] > 4 && SystemClock.elapsedRealtime() - curtime > 300) {
                    num=numUpChange(num);
                    changeTitle(num);
                    cleanTitle(numed);
                    curtime= SystemClock.elapsedRealtime();
                }

                //上转，下翻，数字加
                else if (event.values[0] > 300 && event.values[0] < 651 && SystemClock.elapsedRealtime() - curtime > 300) {
                    num=numDownChange(num);
                    changeTitle(num);
                    cleanTitle(numed);
                    curtime= SystemClock.elapsedRealtime();
                }
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:
//                if (300 > event.values[1] && event.values[1] > 5.5){
//                    Intent intent = new Intent(AlbumActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                    System.out.println("z="+event.values[2]);
//                    System.out.println("y="+event.values[1]);
//                    System.out.println("x="+event.values[0]);
//                }

                if (300 > event.values[2] && event.values[2] > 4&&SystemClock.elapsedRealtime()-curtime1>200){
                    curtime1=SystemClock.elapsedRealtime();
                    System.out.println("up");
                    flag++;
//                    System.out.println("z="+event.values[2]);
//                    System.out.println("y="+event.values[1]);
                }
                //200毫秒防止连带增加flag数
                else if (event.values[2] > 300 && event.values[2] < 652&&SystemClock.elapsedRealtime()-curtime1>200) {
                    curtime1=SystemClock.elapsedRealtime();
                    System.out.println("down");
                    flag++;
                }

                //一秒之后无前进后退响应，上下晃动次数清零
                //设置时间太短，容易没晃完就清零；设置时间太长，容易造成连带
                else if (SystemClock.elapsedRealtime()-curtime1>600)
                    flag=0;

                //返回晃动的阈值
                if (event.values[2] > 300 && event.values[2] < 642){
                    flag1=true;
                }
                //500毫秒用来区分返回和前进（因为不设置延迟的话，假设我要返回，但是晃动的过程中flag到达3就直接前进了）
                //同时500毫秒能防止左右切换的时候连带
                if(flag>=3&&flag<4&&SystemClock.elapsedRealtime()-curtime1>500){
                    flag=0;
                    soundPool.play(1,1, 1, 0, 0, 1);
                    if (num<4) {
                        Intent intent = new Intent(SongListActivity.this, MusicActivity.class);
                        intent.putExtra("num", num);
                        startActivity(intent);
                    }

                }
                else if (flag>=4&&flag1){
                    flag=0;
                    finish();
                }
                break;
        }



    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int numUpChange(int Num){
        if(Num<5) {
            numed=Num;
            Num++;
        }
        else {
            numed = Num;
            Num = 1;
        }
        return Num;
    }
    public int numDownChange(int Num){
        if(Num>1) {
            numed=Num;
            Num--;
        }
        else {
            numed=Num;
            Num = 5;
        }
        return Num;
    }

    public void changeTitle(int Num){
        switch (Num){
            case 1:
                linearLayout1.setBackgroundColor(Color.parseColor("#666666"));
                break;
            case 2:
                linearLayout2.setBackgroundColor(Color.parseColor("#666666"));
                break;
            case 3:
                linearLayout3.setBackgroundColor(Color.parseColor("#666666"));
                break;
            case 4:
                linearLayout4.setBackgroundColor(Color.parseColor("#666666"));
                break;
//            case 5:
//                linearLayout5.setBackgroundColor(Color.parseColor("#666666"));
//                break;
        }
    }

    public void cleanTitle(int Num){
        switch (Num){
            case 1:
                linearLayout1.setBackgroundColor(Color.parseColor("#000000"));
                break;
            case 2:
                linearLayout2.setBackgroundColor(Color.parseColor("#000000"));
                break;
            case 3:
                linearLayout3.setBackgroundColor(Color.parseColor("#000000"));
                break;
            case 4:
                linearLayout4.setBackgroundColor(Color.parseColor("#000000"));
                break;
//            case 5:
//                linearLayout5.setBackgroundColor(Color.parseColor("#ffffff"));
//                break;
        }
    }
    public void initView(int Num){
        switch (Num){
            case 1:
                linearLayout1.setBackgroundColor(Color.parseColor("#666666"));
                break;
            case 2:
                linearLayout2.setBackgroundColor(Color.parseColor("#666666"));
                break;
            case 3:
                linearLayout3.setBackgroundColor(Color.parseColor("#666666"));
                break;
            case 4:
                linearLayout4.setBackgroundColor(Color.parseColor("#666666"));
                break;
//            case 5:
//                linearLayout5.setBackgroundColor(Color.parseColor("#666666"));
//                break;
        }
    }

}