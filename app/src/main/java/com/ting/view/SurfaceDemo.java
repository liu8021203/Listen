package com.ting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceDemo extends SurfaceView implements
        SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Canvas canvas;
    private Path path;

    private Paint mPaintCircle;
    private Paint mPaintLine;
    private Paint mPaintText;

    private int width;
    private int height;
    private float degree;
    private boolean isDrawing = true;

    private SensorManager sensorManager;
    private float lastDegree;

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public SurfaceDemo(Context context) {
        super(context);
    }

    public SurfaceDemo(Context context, AttributeSet attrs) {
        super(context, attrs);

        width = 1100;
        height = 1500;
        // 首先得到一个SurfaceHolder对象
        holder = getHolder();
        // 接着让holder添加Callback监听器
        holder.addCallback(this);

        // path用于画指针
        path = new Path();
        // 画外环的画笔
        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.BLACK);
        mPaintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintCircle.setStrokeWidth(30);
        // 画刻度线的画笔
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.WHITE);
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setStrokeWidth(10);
        // 画文本的画笔
        mPaintText = new Paint();
        mPaintText.setTextSize(70);
        mPaintLine.setTextAlign(Paint.Align.LEFT);
        mPaintText.setColor(Color.WHITE);

        // sensorManager用于管理传感器
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        // 地磁传感器
        Sensor magneticSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // 加速度传感器
        Sensor accelerometerSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 分别给两种传感器注册监听器
        sensorManager.registerListener(listener, magneticSensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener, accelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        /**
         * 在一个线程中进行绘制
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 用一个无限循环，便于随时改变图形
                while (isDrawing) {
                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.BLUE);
                    canvas.drawCircle(width / 2, height / 2, 400, mPaintCircle);
                    for (int i = 0; i <= 35; i++) {
                        canvas.save();
                        canvas.rotate(10 * i + degree, width / 2, height / 2);
                        canvas.drawLine(width / 2, height / 2 - 400, width / 2,
                                height / 2 - 400 + 50, mPaintLine);
                        if (i == 0) {
                            canvas.drawText("N", width / 2,
                                    height / 2 - 400 - 40, mPaintText);

                        } else if (i == 8) {
                            canvas.drawText("N", width / 2,
                                    height / 2 - 400 - 40, mPaintText);
                        } else if (i == 17) {
                            canvas.drawText("N", width / 2,
                                    height / 2 - 400 - 40, mPaintText);
                        } else if (i == 26) {
                            canvas.drawText("N", width / 2,
                                    height / 2 - 400 - 40, mPaintText);
                        }
                        canvas.restore();
                    }
                    // 每当degree发生改变，canvas画布都会转动相应的角度
                    canvas.rotate(degree, width / 2, height / 2);

                    // 绘制指针
                    path.moveTo(width / 2 - 10, height / 2);
                    path.lineTo(width / 2, height / 2 - 150);
                    path.lineTo(width / 2 + 10, height / 2);
                    path.close();
                    canvas.drawPath(path, mPaintLine);
                    // 每次用完canvas，都要调用unlockCanvasAndPost()解锁一次
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    /**
     * 注销 当调用surfaceDestroyed方法时，停止线程中的死循环，并且sensorManager的监听器
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
    }

    // 监听传感器
    private SensorEventListener listener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticValues = event.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            // 得到包含旋转矩阵的R数组
            SensorManager.getRotationMatrix(R, null, accelerometerValues,
                    magneticValues);
            // 计算手机的旋转数据，并将参数存入values数组
            SensorManager.getOrientation(R, values);
            // 将弧度转换为角度
            degree = -(float) Math.toDegrees(values[0]);
            if (Math.abs(degree - lastDegree) > 10) {
                lastDegree = degree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}