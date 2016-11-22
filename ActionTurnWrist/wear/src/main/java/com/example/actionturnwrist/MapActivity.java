package com.example.actionturnwrist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import java.util.Map;


public class MapActivity extends Activity implements SensorEventListener {

    private static final String TAG = "PhotoViewer";
    public static final int RESULT_CODE_NOFOUND = 200;





    private double Lat;
    private double Lng;
    // 百度地图控件
    private MapView mMapView = null;
    // 百度地图对象
    private BaiduMap bdMap;
    private MapStatus mMapStatus;
    private LatLng cenpt;
    private MapStatusUpdate mMapStatusUpdate;


    /**
     * 传感器
     */

    private SensorManager sensorManager = null;
    private Sensor accelerationSensor = null;
    private Sensor linearAccelerationSensor = null;
    private boolean Switch;
    private long curtime=0;
    private long curtime1;
    private long curtime2;
    private long curtime3;
    private int flag;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

//        setContentView(R.layout.activity_main);
        init();




//        iniView();
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.onepiece);

//        imgView = (ImageView) findViewById(R.id.imageiew1);// 获取控件
//        imgView.setImageBitmap(bitmap);// 填充控件
        flag=0;
        Switch=false;

//        dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
//        minZoom();
//        center();
//        imgView.setImageMatrix(matrix);
//        matrix.postScale(5, 5);
        /** 传感器*/

//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }


    private void init() {
        Lat=39.968237;//纬度
        Lng=116.367655;//经度
        mMapView = (MapView) findViewById(R.id.bmapview);
        bdMap = mMapView.getMap();
        cenpt = new LatLng(Lat,Lng);
        //定义地图状态
        mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        bdMap.setMapStatus(mMapStatusUpdate);
        //普通地图
        bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //卫星地图
//        bdMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        // 开启交通图
//        bdMap.setTrafficEnabled(true);
        // 开启热力图
//       bdMap.setBaiduHeatMapEnabled(true);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        sensorManager.registerListener(this,accelerationSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

//    private void iniView(){
//        num=getIntent().getIntExtra("num",0);
//        switch (num){
//            case 0:
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapone);
//                break;
//            case 1:
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.onepiece1);
//                break;
//            case 2:
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.onepiece3);
//                break;
//            case 3:
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.onepiece2);
//                break;
//            case 4:
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.onepiece4);
//                break;
//        }
//    }

//
//    private void CheckView() {
//        float p[] = new float[9];
//        matrix.getValues(p);
//        if (mode == ZOOM) {
//            if (p[0] < minScaleR) {
//                //Log.d("", "当前缩放级别:"+p[0]+",最小缩放级别:"+minScaleR);
//                matrix.setScale(minScaleR, minScaleR);
//            }
//            if (p[0] > MAX_SCALE) {
//                //Log.d("", "当前缩放级别:"+p[0]+",最大缩放级别:"+MAX_SCALE);
//                matrix.set(savedMatrix);
//            }
//        }
//        center();
//    }

    /**
     * 最小缩放比例，最大为100%
     */
//    private void minZoom() {
//        minScaleR = Math.min(
//                (float) dm.widthPixels / (float) bitmap.getWidth(),
//                (float) dm.heightPixels / (float) bitmap.getHeight());
//        if (minScaleR < 1.0) {
//            matrix.postScale(minScaleR, minScaleR);
//        }
//    }

//    private void center() {
//        center(true, true);
//    }

    /**
     * 横向、纵向居中
     */
//    protected void center(boolean horizontal, boolean vertical) {
//        Matrix m = new Matrix();
//        m.set(matrix);
//        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        m.mapRect(rect);
//
//        float height = rect.height();
//        float width = rect.width();
//
//        float deltaX = 0, deltaY = 0;
//
//        if (vertical) {
//            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
//            int screenHeight = dm.heightPixels;
//            if (height < screenHeight) {
//                deltaY = (screenHeight - height) / 2 - rect.top;
//            } else if (rect.top > 0) {
//                deltaY = -rect.top;
//            } else if (rect.bottom < screenHeight) {
//                deltaY = screenHeight - rect.bottom;
//            }
//
//        }
//
//        if (horizontal) {
//            int screenWidth = dm.widthPixels;
//            if (width < screenWidth) {
//                deltaX = (screenWidth - width) / 2 - rect.left;
//            } else if (rect.left > 0) {
//                deltaX = -rect.left;
//            } else if (rect.right < screenWidth) {
//                deltaX = screenWidth - rect.right;
//            }
//        }
//        matrix.postTranslate(deltaX, deltaY);
//    }



    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onResume() {
        super.onResume();
        mMapView.onResume();
        sensorManager.registerListener(this, accelerationSensor,SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, linearAccelerationSensor,SensorManager.SENSOR_DELAY_FASTEST);
        curtime1=0;
        curtime2=0;
        curtime2=0;
//        System.out.println("what???????????????????????");

    }

    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    //
    @Override
    public void onSensorChanged(SensorEvent event) {
//        mTextView.setText("x="+x+ "," + "y="+ y + "," + "z=" + z);
//        if(SystemClock.elapsedRealtime()-curtime>1000)
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if(Switch==false){
                    moveMap(event.values[0], event.values[1]);
//                    center();
                }
//                else if (Switch==true){
//                    transformMap(event.values[2]);
////                    center();
//                }
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
//                if (300 > event.values[1] && event.values[1] > 5.5){
//                    Intent intent = new Intent(MapActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }

                if (300 > event.values[2] && event.values[2] > 4&& SystemClock.elapsedRealtime()-curtime1>200){
                    curtime1=SystemClock.elapsedRealtime();
                    System.out.println("up");
                    flag++;
                }
                else if (event.values[2] > 300 && event.values[2] < 652&&SystemClock.elapsedRealtime()-curtime1>200) {
                    curtime1=SystemClock.elapsedRealtime();
                    System.out.println("down");
                    flag++;
                }
                else if (SystemClock.elapsedRealtime()-curtime1>1000)
                    flag=0;

                if(flag>=3&&flag<4&&SystemClock.elapsedRealtime()-curtime1>500){
                    flag=0;
                    System.out.println("YES");
                    if (Switch)
                        Switch=false;
                    else
                        Switch=true;
                }
                else if (flag>=4){
                    flag=0;
                    System.out.println("NO");
//                    Switch=false;
                    finish();
                }
                break;
        }

    }


    public void moveMap(float x,float y){
        if (y>1&&y<300)
            Lat=Lat+0.0001;
        else if(y>300&&y<655) {
            Lat=Lat-0.0001;
        }
        if (x>1&&x<300)
            Lng=Lng+0.0001;
        else if(x>300&&x<654)
            Lng=Lng-0.0001;
        cenpt = new LatLng(Lat,Lng);
        mMapStatus = new MapStatus.Builder().target(cenpt).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        bdMap.setMapStatus(mMapStatusUpdate);
    }



    public void  transformMap(float z){
        if(z>11&&z<300&&SystemClock.elapsedRealtime()-curtime3>100){

//            matrix.postScale(1.03f,1.03f);
//            matrix.postScale(1.5f,1.5f);
            curtime2=SystemClock.elapsedRealtime();
        }
        else if(z<9.2&&SystemClock.elapsedRealtime()-curtime2>100){


//            matrix.postScale(0.6f,0.6f);

            curtime3=SystemClock.elapsedRealtime();
        }

//        System.out.println(z);


    }





    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

