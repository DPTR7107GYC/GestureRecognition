package com.example.actionturnwrist;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;

import android.provider.ContactsContract;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MainActivity extends Activity implements SensorEventListener {



    private SensorManager sensorManager = null;
    private Sensor gyroscopeSensor = null;
    private Sensor lineraccelerateSensor = null;
    private int flag = 0;
    private long curtime;
    private long curtime1;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private int num = 1;
    private boolean flag1 = false;
    private SoundPool soundPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout1 = (LinearLayout) findViewById(R.id.linearlayout1) ;
        linearLayout2 = (LinearLayout)findViewById(R.id.linearlayout2) ;
        linearLayout3 = (LinearLayout)findViewById(R.id.linearlayout3) ;
        linearLayout4 = (LinearLayout)findViewById(R.id.linearlayout4) ;
        iv1=(ImageView)findViewById(R.id.iv_main_1) ;
        iv2=(ImageView)findViewById(R.id.iv_main_2) ;
        iv3=(ImageView)findViewById(R.id.iv_main_3) ;
        iv4=(ImageView)findViewById(R.id.iv_main_4) ;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        lineraccelerateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
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
        sensorManager.registerListener(this, lineraccelerateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        curtime=0;
        curtime1=0;
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

                //左倾，下翻，数字加
                if (event.values[1] > 300 && event.values[1] < 653 && SystemClock.elapsedRealtime() - curtime > 300) {
                    rowChange(num);
                    curtime= SystemClock.elapsedRealtime();

                } else if (event.values[1] < 300 && event.values[1] > 4 && SystemClock.elapsedRealtime() - curtime > 300) {
                    rowChange(num);
                    curtime= SystemClock.elapsedRealtime();
                }
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:



                if (300 > event.values[2] && event.values[2] > 4&&SystemClock.elapsedRealtime()-curtime1>200){
                    curtime1=SystemClock.elapsedRealtime();
                    System.out.println("up");
                    flag++;
//                    System.out.println("z="+event.values[2]);
//                    System.out.println("y="+event.values[1]);

                }
                else if (event.values[2] > 300 && event.values[2] < 652&&SystemClock.elapsedRealtime()-curtime1>200) {
                    curtime1=SystemClock.elapsedRealtime();
                    System.out.println("down");
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
                    switch (num){
                        case 1:
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, AlbumActivity.class);
                            MainActivity.this.startActivity(intent);
                            break;
                        case 2:
                            Intent intent2 = new Intent();
                            intent2.setClass(MainActivity.this, SongListActivity.class);
                            MainActivity.this.startActivity(intent2);
                            break;
                        case 3:
                            Intent intent3 = new Intent();
                            intent3.setClass(MainActivity.this, MapActivity.class);
                            MainActivity.this.startActivity(intent3);
                            break;
                        case 4:
                            break;
                    }

                }
                else if (flag>=4&&flag1){
                    flag=0;
                    Intent intent = new Intent(MainActivity.this,SoundService.class);
                    stopService(intent);
//                    mTextView1.setText("action is NO");
                }
                break;
        }



    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void lineChange(int Num){
        switch (Num){
            case 1:
                iv3.setImageResource(R.drawable.maphover);
                iv1.setImageResource(R.drawable.album2);
                num=3;
                break;
            case 2:
                iv4.setImageResource(R.drawable.otherhover);
                iv2.setImageResource(R.drawable.music);
                num=4;
                break;
            case 3:
                iv1.setImageResource(R.drawable.albumhover);
                iv3.setImageResource(R.drawable.map);
                num=1;
                break;
            case 4:
                iv2.setImageResource(R.drawable.musichover);
                iv4.setImageResource(R.drawable.other);
                num=2;
                break;
        }
    }
    private void rowChange(int Num){
        switch (Num){
            case 1:
                iv2.setImageResource(R.drawable.musichover);
                iv1.setImageResource(R.drawable.album2);
                num=2;
                break;
            case 2:
                iv1.setImageResource(R.drawable.albumhover);
                iv2.setImageResource(R.drawable.music);
                num=1;
                break;
            case 3:
                iv4.setImageResource(R.drawable.otherhover);
                iv3.setImageResource(R.drawable.map);
                num=4;
                break;
            case 4:
                iv3.setImageResource(R.drawable.maphover);
                iv4.setImageResource(R.drawable.other);
                num=3;
                break;
        }
    }
    public void initView(int Num){
        switch (Num){
            case 1:
                iv1.setImageResource(R.drawable.albumhover);
                break;
            case 2:
                iv2.setImageResource(R.drawable.musichover);
                break;
            case 3:
                iv3.setImageResource(R.drawable.maphover);
                break;
            case 4:
                iv4.setImageResource(R.drawable.otherhover);
                break;
        }
    }
}