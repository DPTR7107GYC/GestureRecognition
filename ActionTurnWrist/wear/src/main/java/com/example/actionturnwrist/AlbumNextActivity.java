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
import android.widget.ImageView;
import android.widget.LinearLayout;


public class AlbumNextActivity extends Activity implements SensorEventListener {



    private SensorManager sensorManager = null;
    private Sensor gyroscopeSensor = null;
    private Sensor linearAccelerationSensor = null;
    private int flag = 0;
    private long curtime;
    private long curtime1;
    private long curtime2;
    private ImageView pic=null;
    private ImageView pic1=null;
    private ImageView pic2=null;
    private ImageView pic3=null;
    private ImageView pic4=null;
    private int num = 0;
    private boolean flag1 = false;
    private SoundPool soundPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_next);
        pic= (ImageView) findViewById(R.id.pic);
        pic1= (ImageView) findViewById(R.id.pic1);
        pic2= (ImageView) findViewById(R.id.pic2);
        pic3= (ImageView) findViewById(R.id.pic3);
        pic4= (ImageView) findViewById(R.id.pic4);

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
                    lineChange(num);
                    curtime= SystemClock.elapsedRealtime();
                }

                //上转，下翻，数字加
                else if (event.values[0] > 300 && event.values[0] < 651 && SystemClock.elapsedRealtime() - curtime > 300) {
                    lineChange(num);
                    curtime= SystemClock.elapsedRealtime();
                }
                if (event.values[1] > 300 && event.values[1] < 653 && SystemClock.elapsedRealtime() - curtime > 300) {
                    leftChange(num);
                    curtime= SystemClock.elapsedRealtime();

                }
                else if (event.values[1] < 300 && event.values[1] > 4 && SystemClock.elapsedRealtime() - curtime > 300) {
                    rightChange(num);
                    curtime= SystemClock.elapsedRealtime();
                }
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:
//                if (300 > event.values[1] && event.values[1] > 5.5){
//                    Intent intent = new Intent(AlbumNextActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
                if (300 > event.values[2] && event.values[2] > 4&&SystemClock.elapsedRealtime()-curtime1>200){
                    curtime1=SystemClock.elapsedRealtime();
                    System.out.println("up");
//                    System.out.println(event.values[2]);
                    flag++;
                }
                else if (event.values[2] > 300 && event.values[2] < 652&&SystemClock.elapsedRealtime()-curtime1>200) {
                    curtime1=SystemClock.elapsedRealtime();
                    System.out.println("down");
//                    System.out.println(event.values[2]);
                    flag++;
                }

                else if (SystemClock.elapsedRealtime()-curtime1>600)
                    flag=0;

                if (event.values[2] > 300 && event.values[2] < 642){
                    flag1=true;
                }


                if(flag>=3&&flag<4&&SystemClock.elapsedRealtime()-curtime1>500){
                    flag=0;
                    soundPool.play(1,1, 1, 0, 0, 1);
                    Intent intent = new Intent();
                    intent.putExtra("num",num);
                    intent.setClass(AlbumNextActivity.this, PictureActivity.class);
                    AlbumNextActivity.this.startActivity(intent);

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

    private void lineChange(int Num){
        switch (Num){
            case 0:
                pic3.setImageResource(R.drawable.onepiece2hover);
                pic.setImageResource(R.drawable.onepiece);
                num=3;
                break;
            case 1:
                pic4.setImageResource(R.drawable.onepiece4hover);
                pic1.setImageResource(R.drawable.onepiece1);
                num=4;
                break;
            case 2:
                break;
            case 3:
                pic.setImageResource(R.drawable.onepiecehover);
                pic3.setImageResource(R.drawable.onepiece2);
                num=0;
                break;
            case 4:
                pic1.setImageResource(R.drawable.onepiece1hover);
                pic4.setImageResource(R.drawable.onepiece4);
                num=1;
                break;
        }
    }

    private void leftChange(int Num){
        switch (Num){
            case 0:
                pic2.setImageResource(R.drawable.onepiece3hover);
                pic.setImageResource(R.drawable.onepiece);
                num=2;
                break;
            case 1:
                pic.setImageResource(R.drawable.onepiecehover);
                pic1.setImageResource(R.drawable.onepiece1);
                num=0;
                break;
            case 2:
                pic1.setImageResource(R.drawable.onepiece1hover);
                pic2.setImageResource(R.drawable.onepiece3);
                num=1;
                break;
            case 3:
                pic4.setImageResource(R.drawable.onepiece4hover);
                pic3.setImageResource(R.drawable.onepiece2);
                num=4;
                break;
            case 4:
                pic3.setImageResource(R.drawable.onepiece2hover);
                pic4.setImageResource(R.drawable.onepiece4);
                num=3;
                break;
        }
    }

    private void rightChange(int Num){
        switch (Num){
            case 0:
                pic1.setImageResource(R.drawable.onepiece1hover);
                pic.setImageResource(R.drawable.onepiece);
                num=1;
                break;
            case 1:
                pic2.setImageResource(R.drawable.onepiece3hover);
                pic1.setImageResource(R.drawable.onepiece1);
                num=2;
                break;
            case 2:
                pic.setImageResource(R.drawable.onepiecehover);
                pic2.setImageResource(R.drawable.onepiece3);
                num=0;
                break;
            case 3:
                pic4.setImageResource(R.drawable.onepiece4hover);
                pic3.setImageResource(R.drawable.onepiece2);
                num=4;
                break;
            case 4:
                pic3.setImageResource(R.drawable.onepiece2hover);
                pic4.setImageResource(R.drawable.onepiece4);
                num=3;
                break;
        }
    }
    public void initView(int Num){
        switch (Num){
            case 0:
                pic.setImageResource(R.drawable.onepiecehover);
                break;
            case 1:
                pic1.setImageResource(R.drawable.onepiece1hover);
                break;
            case 2:
                pic2.setImageResource(R.drawable.onepiece3hover);
                break;
            case 3:
                pic3.setImageResource(R.drawable.onepiece2hover);
                break;
            case 4:
                pic4.setImageResource(R.drawable.onepiece4hover);
                break;
        }
    }

}