package com.example.actionturnwrist;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import static com.example.actionturnwrist.R.id.start;

/**
 * Created by Administrator on 2016/8/14.
 */
public class MusicActivity extends Activity implements SensorEventListener{

    private SensorManager sensorManager = null;
    private Sensor gyroscopeSensor = null;
    private Sensor linearAccelerationSensor = null;
    private ImageView btnPlay = null;
    private ImageView next = null;
    private ImageView previous = null;
    private ImageView note = null;
    private TextView songname = null;
    private TextView singername = null;
    private boolean flag3 = false;
    private int flag = 0;
    private long curtime;
    private long curtime1;
    private int num = 1;
    private boolean flag1 = false;
    private RotateAnimation animation = null;
    private SeekBar sb;
    private ServiceConnection serviceConnection;
    private SoundService.Mybind mybind;
    private TextView time_passed = null;
    private TextView time_total = null;
    private int pc;
    private Handler mHandler;
    private String maxTime ;
    private String pcTime ;






//    Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg){
//            switch (msg.what){
//                case 0:
//                    //更新进度
//                    int position = SoundService.Mybind.;
//
//                    int time = SoundService.Mybind.get();
//                    int max = seekBar.getMax();
//
//                    seekBar.setProgress(position*max/time);
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    };














    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        btnPlay = (ImageView)findViewById(R.id.btnPlay);
        note=(ImageView)findViewById(R.id.note);
        next = (ImageView)findViewById(R.id.next);
        previous = (ImageView)findViewById(R.id.previous);
        songname = (TextView)findViewById(R.id.songname);
        singername = (TextView)findViewById(R.id.singername);
        btnPlay.setBackgroundResource(R.drawable.play);
        sb=(SeekBar)findViewById(R.id.sb);
        time_passed=(TextView)findViewById(R.id.time_passed);
        time_total=(TextView)findViewById(R.id.time_total);



        animation = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(10000);//设定转一圈的时间
        animation.setRepeatCount(Animation.INFINITE);//设定无限循环
        animation.setRepeatMode(Animation.INFINITE);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);






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
        num=getIntent().getIntExtra("num",1);
        changeTitle(num);
        play();

    }




    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_GYROSCOPE:

                if (event.values[1] > 300 && event.values[1] < 653 && SystemClock.elapsedRealtime() - curtime > 1000) {
                    num=numDownChange(num);
                    changeTitle(num);
                    flag3=flagChange(flag3);
                    play();
                    curtime= SystemClock.elapsedRealtime();

                }
                else if (event.values[1] < 300 && event.values[1] > 4 && SystemClock.elapsedRealtime() - curtime > 1000) {

                    num=numUpChange(num);
                    changeTitle(num);
                    flag3=flagChange(flag3);
                    play();
                    curtime= SystemClock.elapsedRealtime();
                }
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:
//                if (300 > event.values[1] && event.values[1] > 5.5){
//                    Intent intent = new Intent(MusicActivity.this,MainActivity.class);
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
                    play();

                }
                else if (flag>=4&&flag1){
                    flag=0;
                    finish();
                }
                break;
        }



    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    public int numUpChange(int Num){
        if(Num<3)
        Num++;
        else
        Num=1;
        return Num;
    }
    public int numDownChange(int Num){
        if(Num>1)
            Num--;
        else
            Num=3;
        return Num;
    }

    public void changeTitle(int Num){
        switch (Num){
            case 1:
                songname.setText("SOBER");
                singername.setText("BIGBANG");
                note.setBackgroundResource(R.drawable.sober);
                break;
            case 2:
                songname.setText("我的天空");
                singername.setText("南征北战");
                note.setBackgroundResource(R.drawable.mysky);
                break;
            case 3:
                songname.setText("UpTownFunk");
                singername.setText("BrunoMars");
                note.setBackgroundResource(R.drawable.uptownfunk);
                break;
            default:
                break;
        }
    }


    public void play(){
        Intent intent = new Intent(MusicActivity.this,SoundService.class);
        if (flag3==false){
            intent.putExtra("playing", true);
            intent.putExtra("id",num);
            startService(intent);
//            System.out.println("123123123");
            btnPlay.setBackgroundResource(R.drawable.pause);
            flag3=true;
            note.startAnimation(animation);
            serviceConnection=null;
            play_connection(intent);
        }
        else{
            intent.putExtra("playing", false);
            intent.putExtra("id",num);
            startService(intent);
//            System.out.println("456456456");
            btnPlay.setBackgroundResource(R.drawable.play);
            flag3=false;
            note.clearAnimation();
            serviceConnection=null;
//            play_connection(intent);
        }
    }
    public boolean flagChange(boolean Flag){
        if(Flag)
            Flag=false;
        else
            Flag=true;
        return Flag;
    }


    public void play_connection(Intent intent){
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                    mybind = (SoundService.Mybind) service;

                    //设置进度条的最大长度
                    final int max = mybind.getMusicDuration();

                    sb.setMax(max);
//                    sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                        @Override
//                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                            mybind.seekTo(progress);
//                        }
//
//                        @Override
//                        public void onStartTrackingTouch(SeekBar seekBar) {
//
//                        }
//
//                        @Override
//                        public void onStopTrackingTouch(SeekBar seekBar) {
//
//                        }
//                    });




                    new Thread() {
                        public void run() {
                            //这儿是耗时操作，完成之后更新UI；
                            int numThread=num;
                            do{
                                runOnUiThread(new Runnable(){

                                    @Override
                                    public void run() {
                                        maxTime = (int)(Math.floor(Math.floor(max/1000)/60))+":"+(int)(Math.floor(max/1000)%60);
                                        pcTime = (int)(Math.floor(Math.floor(pc/1000)/60))+":"+(int)(Math.floor(pc/1000)%60);
                                        time_total.setText(maxTime);
                                        time_passed.setText(pcTime);

                                    }


                                });
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                            }while(numThread==num&&flag3==true);

                        }
                    }.start();





                    //连接之后启动子线程设置当前进度
                    new Thread()
                    {
                        public void run()
                        {
                            int numThread=num;
                            //改变当前进度条的值
                            //设置当前进度
                            do {
                                pc=mybind.getMusicCurrentPosition();
                                 sb.setProgress(pc);
//                                time_total.setText(max);
//                                time_passed.setText(pc);
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                            }while(numThread==num&&flag3==true);
                        }

                    }.start();




                }





                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            //以绑定方式连接服务
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        }
    }
}

