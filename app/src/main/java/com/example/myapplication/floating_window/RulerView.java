package com.example.myapplication.floating_window;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

public class RulerView extends View {

    private Paint paint;
    private int color = Color.WHITE;

    public RulerView(Context context) {
        super(context);
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RulerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();


        drawScale(canvas, width, height);//绘制刻度
        drawOutline(canvas, width, height);//绘制轮廓

    }

    //绘制刻度
    private void drawScale(Canvas canvas, int width, int height) {

        paint.setStrokeWidth(4);
        paint.setTextSize(28);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);

        int t = 13;                //设置尺度为 13 cm
        int w = width / t;        //获取每个尺度对应的宽度
        int h = height / 3;       //获取当前尺子1/3的的高度
        w = (w / 2);              //定义尺子刻度从当前绘画
        int minW = ((w + (width / t)) - w) / 10;    //设置每 1cm 中间的 1mm之间的距离
        int sum;    //定义累加记录
        for (int i = 0; i < t; i++) {

            if (i + 1 < 10) {
                if (i == 0) {
                    canvas.drawText("u", w + 10, height / 4, paint);//绘画刻度值
                }
                canvas.drawText(String.valueOf(i), w - 8, height / 4, paint);//绘画刻度值
            } else {
                canvas.drawText(String.valueOf(i), w - 16, height / 4, paint);//绘画刻度值
            }

            paint.setStrokeWidth(4);
            paint.setColor(color);
            canvas.drawLine(w, height, w, h, paint);    //画出 1cm 尺度
            sum = w + 1;      //初始化 累加的初值并跳过 1 cm 刻度的位置向前绘画
            if (i < t - 1)     //设置绘画 1mm 的刻度只画 12 次
                for (int j = 0; j < 10; j++) {    //每一 1cm 中间画 10 个1mm
                    sum += minW;//再累加刻度
                    if (j == 5) {
                        paint.setStrokeWidth(4);
                        canvas.drawLine(sum, height, sum, (h * 2) / 1.3f, paint);//绘画 小刻度 中间的第5mm
                    } else {
                        paint.setStrokeWidth(2);
                        canvas.drawLine(sum, height, sum, h * 2, paint);//绘画 小刻度 1mm
                    }
                }
            w += (width / t);
        }
        canvas.save();

    }

    //绘制轮廓
    private void drawOutline(Canvas canvas, int width, int height) {
        /**
         * rx 与 ry 是用来设置 角度的
         */
        paint.setStrokeWidth(4);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(new RectF(0, 0, width, height), 10, 10, paint);
        canvas.save();
    }

}
