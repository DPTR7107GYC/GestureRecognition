package com.example.actionturnwrist;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import javax.sql.DataSource;

public class SoundService extends Service {
    private MediaPlayer mp;
    private int num;


    public class Mybind extends Binder
    {
        //获取歌曲长度
        public int getMusicDuration()
        {
            int rtn = 0;
            if (mp != null)
            {
                rtn = mp.getDuration();
            }

            return rtn;
        }
        //获取当前播放进度
        public int getMusicCurrentPosition()
        {
            int rtn = 0;
            if (mp != null)
            {
                rtn = mp.getCurrentPosition();

            }

            return rtn;
        }

        public void seekTo(int position)
        {
            if (mp != null)
            {
                mp.seekTo(position);
            }
        }

    }

    @Override
    public void onCreate() {
        System.out.println("create");
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.sober);



//        final int milliseconds = 100;
//        new Thread(){
//            @Override
//            public void run(){
//                while(true){
//                    try {
//                        sleep(milliseconds);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                    mHandler.sendEmptyMessage(0);
//                }
//            }
//        }.start();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("start");
        boolean playing = intent.getBooleanExtra("playing", false);
        num=intent.getIntExtra("id",2);
        if (num==1) {
//            mp.stop();
            mp.release();
            mp = MediaPlayer.create(this, R.raw.sober);

        }
        else if (num==2){
//            mp.stop();
            mp.release();
            mp = MediaPlayer.create(this, R.raw.mysky);
        }
        else if (num==3){
//            mp.stop();
            mp.release();
            mp = MediaPlayer.create(this, R.raw.uptownfunk);
        }


        if (playing) {
            mp.start();
        } else {
            mp.pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Mybind();
    }

}